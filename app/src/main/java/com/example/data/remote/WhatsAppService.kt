package com.example.data.remote

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.data.model.FoodDonation
import com.example.data.model.FoodRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object WhatsAppService {
    const val SUPPORT_PHONE_NUMBER = "+917548813430"
    const val CLEAN_SUPPORT_PHONE = "917548813430"

    private val httpClient = OkHttpClient()

    /**
     * Creates a WhatsApp direct chat URI with safe URL encoding
     */
    fun createWhatsAppChatUri(phone: String = CLEAN_SUPPORT_PHONE, message: String): Uri {
        val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
        val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
        return Uri.parse("https://wa.me/$cleanPhone?text=$encodedMessage")
    }

    /**
     * Opens WhatsApp application or browser to chat with prefilled message
     */
    fun launchWhatsAppChat(context: Context, phone: String = CLEAN_SUPPORT_PHONE, message: String) {
        val uri = createWhatsAppChatUri(phone, message)
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            setPackage("com.whatsapp")
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(fallbackIntent)
            } catch (e: Exception) {
                Log.e("WhatsAppService", "Failed to open WhatsApp: ${e.message}")
            }
        }
    }

    /**
     * Alias for opening chat
     */
    fun openChat(context: Context, phone: String = SUPPORT_PHONE_NUMBER, message: String) {
        launchWhatsAppChat(context, phone, message)
    }

    /**
     * Share text intent to WhatsApp or chooser
     */
    fun shareTextToWhatsApp(context: Context, text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            setPackage("com.whatsapp")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(sendIntent)
        } catch (_: Exception) {
            val chooser = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }, "Share via MealBridge").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(chooser)
        }
    }

    /**
     * Share donation via WhatsApp
     */
    fun shareDonation(context: Context, donation: FoodDonation) {
        val message = "🌱 MealBridge Food Donation Alert!\n\n" +
                "Food: ${donation.food_name} (${donation.food_type})\n" +
                "Donation ID: ${donation.donation_id}\n" +
                "Quantity: ${donation.quantity} (Serves ${donation.people_served} people)\n" +
                "Location: ${donation.pickup_address}, ${donation.city}\n" +
                "Safe Until: ${donation.expiry_date}\n\n" +
                "Join MealBridge to request this food or connect on WhatsApp (+91 7548813430)."
        shareTextToWhatsApp(context, message)
    }

    /**
     * Share food request via WhatsApp
     */
    fun shareFoodRequest(context: Context, req: FoodRequest) {
        val message = "🚨 MealBridge Urgent Food Request!\n\n" +
                "Request ID: ${req.request_id}\n" +
                "Food Category: ${req.category}\n" +
                "Quantity Needed: ${req.quantity} (For ${req.people_count} people)\n" +
                "Location: ${req.location}\n" +
                "Required By: ${req.required_date} at ${req.required_time}\n" +
                "Reason: ${req.reason}\n\n" +
                "Can you help bridge this meal? Contact MealBridge Support (+91 7548813430)."
        shareTextToWhatsApp(context, message)
    }

    /**
     * Server-side notification service abstraction for official WhatsApp Business Cloud API.
     */
    suspend fun sendWhatsAppNotification(
        recipientPhone: String,
        templateName: String,
        bodyText: String,
        apiToken: String? = null,
        phoneNumberId: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        if (apiToken.isNullOrBlank() || phoneNumberId.isNullOrBlank()) {
            Log.i("WhatsAppService", "WhatsApp API credentials not configured. Notification preserved locally.")
            return@withContext false
        }

        try {
            val url = "https://graph.facebook.com/v18.0/$phoneNumberId/messages"
            val jsonPayload = """
                {
                    "messaging_product": "whatsapp",
                    "recipient_type": "individual",
                    "to": "$recipientPhone",
                    "type": "text",
                    "text": { "preview_url": false, "body": "$bodyText" }
                }
            """.trimIndent()

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $apiToken")
                .addHeader("Content-Type", "application/json")
                .post(jsonPayload.toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

            val response = httpClient.newCall(request).execute()
            val isSuccess = response.isSuccessful
            response.close()
            isSuccess
        } catch (e: Exception) {
            Log.e("WhatsAppService", "WhatsApp Cloud API error: ${e.message}")
            false
        }
    }
}
