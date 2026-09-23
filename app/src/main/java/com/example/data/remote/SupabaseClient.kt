package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.data.model.ContactMessage
import com.example.data.model.FoodDonation
import com.example.data.model.FoodRequest
import com.example.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class SupabaseClient(private val context: Context) {
    private val prefs = context.getSharedPreferences("mealbridge_supabase_prefs", Context.MODE_PRIVATE)
    private val httpClient = OkHttpClient()

    var supabaseUrl: String
        get() = prefs.getString("supabase_url", "https://your-project.supabase.co") ?: "https://your-project.supabase.co"
        set(value) = prefs.edit().putString("supabase_url", value.trim().removeSuffix("/")).apply()

    var supabaseAnonKey: String
        get() = prefs.getString("supabase_anon_key", "your-anon-key") ?: "your-anon-key"
        set(value) = prefs.edit().putString("supabase_anon_key", value.trim()).apply()

    val isConfigured: Boolean
        get() = !supabaseUrl.contains("your-project") && !supabaseAnonKey.contains("your-anon-key") && supabaseUrl.startsWith("https://")

    suspend fun testConnection(): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext Pair(false, "Supabase credentials not configured yet. Running in offline-first permanent local database mode.")
        }
        try {
            val url = "$supabaseUrl/rest/v1/donations?select=count"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", supabaseAnonKey)
                .addHeader("Authorization", "Bearer $supabaseAnonKey")
                .addHeader("Range", "0-0")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            val code = response.code
            val body = response.body?.string() ?: ""
            response.close()

            if (code in 200..299) {
                Pair(true, "Successfully connected to Supabase PostgreSQL database!")
            } else {
                Pair(false, "Connected but returned status $code: $body")
            }
        } catch (e: Exception) {
            Log.e("SupabaseClient", "Connection error: ${e.message}")
            Pair(false, "Connection error: ${e.localizedMessage}")
        }
    }

    suspend fun syncDonationToCloud(donation: FoodDonation): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                put("id", donation.id)
                put("donation_id", donation.donation_id)
                put("donor_id", donation.donor_id)
                put("food_name", donation.food_name)
                put("category", donation.category)
                put("quantity", donation.quantity)
                put("people_served", donation.people_served)
                put("food_type", donation.food_type)
                put("preparation_date", donation.preparation_date)
                put("expiry_date", donation.expiry_date)
                put("pickup_address", donation.pickup_address)
                put("city", donation.city)
                put("contact_number", donation.contact_number)
                put("description", donation.description)
                put("image_url", donation.image_url)
                put("status", donation.status)
            }

            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/donations")
                .addHeader("apikey", supabaseAnonKey)
                .addHeader("Authorization", "Bearer $supabaseAnonKey")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "resolution=merge-duplicates")
                .post(json.toString().toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

            val response = httpClient.newCall(request).execute()
            val ok = response.isSuccessful
            response.close()
            ok
        } catch (e: Exception) {
            Log.e("SupabaseClient", "Failed to sync donation: ${e.message}")
            false
        }
    }

    suspend fun syncFoodRequestToCloud(req: FoodRequest): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                put("id", req.id)
                put("request_id", req.request_id)
                put("requester_id", req.requester_id)
                put("category", req.category)
                put("quantity", req.quantity)
                put("people_count", req.people_count)
                put("required_date", req.required_date)
                put("required_time", req.required_time)
                put("location", req.location)
                put("contact_number", req.contact_number)
                put("reason", req.reason)
                put("notes", req.notes)
                put("status", req.status)
            }

            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/food_requests")
                .addHeader("apikey", supabaseAnonKey)
                .addHeader("Authorization", "Bearer $supabaseAnonKey")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "resolution=merge-duplicates")
                .post(json.toString().toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

            val response = httpClient.newCall(request).execute()
            val ok = response.isSuccessful
            response.close()
            ok
        } catch (e: Exception) {
            Log.e("SupabaseClient", "Failed to sync request: ${e.message}")
            false
        }
    }

    suspend fun syncContactMessageToCloud(msg: ContactMessage): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext false
        try {
            val json = JSONObject().apply {
                put("id", msg.id)
                put("name", msg.name)
                put("email", msg.email)
                put("phone", msg.phone)
                put("subject", msg.subject)
                put("message", msg.message)
            }

            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/contact_messages")
                .addHeader("apikey", supabaseAnonKey)
                .addHeader("Authorization", "Bearer $supabaseAnonKey")
                .addHeader("Content-Type", "application/json")
                .post(json.toString().toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

            val response = httpClient.newCall(request).execute()
            val ok = response.isSuccessful
            response.close()
            ok
        } catch (e: Exception) {
            Log.e("SupabaseClient", "Failed to sync contact message: ${e.message}")
            false
        }
    }
}
