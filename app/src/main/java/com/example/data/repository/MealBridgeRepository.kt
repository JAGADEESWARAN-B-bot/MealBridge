package com.example.data.repository

import android.content.Context
import com.example.data.local.MealBridgeDao
import com.example.data.model.AppNotification
import com.example.data.model.ContactMessage
import com.example.data.model.DonationRequest
import com.example.data.model.FoodDonation
import com.example.data.model.FoodRequest
import com.example.data.model.UserProfile
import com.example.data.remote.SupabaseClient
import com.example.data.remote.WhatsAppService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class MealBridgeRepository(
    private val context: Context,
    private val dao: MealBridgeDao,
    private val supabaseClient: SupabaseClient,
    private val scope: CoroutineScope
) {
    private val sessionPrefs = context.getSharedPreferences("mealbridge_auth_session", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    init {
        // Restore existing user session if any
        val savedUserId = sessionPrefs.getString("logged_in_user_id", null)
        if (savedUserId != null) {
            scope.launch(Dispatchers.IO) {
                val profile = dao.getProfileById(savedUserId)
                _currentUser.value = profile
            }
        }
    }

    // --- Authentication ---
    suspend fun registerUser(
        fullName: String,
        email: String,
        phone: String,
        userType: String,
        address: String
    ): Result<UserProfile> {
        val existing = dao.getProfileByEmail(email)
        if (existing != null) {
            return Result.failure(Exception("An account with this email already exists."))
        }

        val newProfile = UserProfile(
            id = UUID.randomUUID().toString(),
            full_name = fullName.trim(),
            email = email.trim(),
            phone = phone.trim(),
            user_type = userType,
            address = address.trim(),
            status = "Active",
            is_admin = email.trim().equals("admin@mealbridge.org", ignoreCase = true)
        )
        dao.insertProfile(newProfile)

        // Add welcome notification
        dao.insertNotification(
            AppNotification(
                user_id = newProfile.id,
                title = "Welcome to MealBridge",
                message = "Your profile has been created successfully. Start sharing food and reducing waste today!"
            )
        )

        return Result.success(newProfile)
    }

    suspend fun loginUser(email: String): Result<UserProfile> {
        val user = dao.getProfileByEmail(email.trim())
        return if (user != null) {
            if (user.status == "Inactive") {
                Result.failure(Exception("Your account has been deactivated. Please contact MealBridge support on WhatsApp."))
            } else {
                _currentUser.value = user
                sessionPrefs.edit().putString("logged_in_user_id", user.id).apply()
                Result.success(user)
            }
        } else {
            Result.failure(Exception("No account found with this email. Please check or register a new account."))
        }
    }

    fun logout() {
        _currentUser.value = null
        sessionPrefs.edit().remove("logged_in_user_id").apply()
    }

    suspend fun updateProfile(name: String, phone: String, address: String): Result<UserProfile> {
        val current = _currentUser.value ?: return Result.failure(Exception("User not logged in"))
        val updated = current.copy(
            full_name = name.trim(),
            phone = phone.trim(),
            address = address.trim()
        )
        dao.updateProfile(updated)
        _currentUser.value = updated
        return Result.success(updated)
    }

    // --- Donations ---
    val allDonations: Flow<List<FoodDonation>> = dao.getAllDonationsFlow()
    val availableDonations: Flow<List<FoodDonation>> = dao.getAvailableDonationsFlow()

    fun getMyDonations(userId: String): Flow<List<FoodDonation>> = dao.getDonationsByDonorFlow(userId)

    fun getDonationById(id: String): Flow<FoodDonation?> = dao.getDonationByIdFlow(id)

    suspend fun createDonation(
        foodName: String,
        category: String,
        quantity: String,
        peopleServed: Int,
        foodType: String,
        preparationDate: String,
        expiryDate: String,
        pickupAddress: String,
        city: String,
        contactNumber: String,
        description: String,
        imageUrl: String
    ): Result<FoodDonation> {
        val user = _currentUser.value ?: return Result.failure(Exception("Please log in to submit a donation"))

        val donation = FoodDonation(
            donor_id = user.id,
            donor_name = user.full_name,
            food_name = foodName.trim(),
            category = category,
            quantity = quantity.trim(),
            people_served = peopleServed,
            food_type = foodType,
            preparation_date = preparationDate.trim(),
            expiry_date = expiryDate.trim(),
            pickup_address = pickupAddress.trim(),
            city = city.trim(),
            contact_number = contactNumber.trim(),
            description = description.trim(),
            image_url = imageUrl,
            status = "Active"
        )

        dao.insertDonation(donation)

        // Notification for donor
        dao.insertNotification(
            AppNotification(
                user_id = user.id,
                title = "Donation Submitted: ${donation.donation_id}",
                message = "Your donation of ${donation.food_name} ($quantity) has been successfully listed in $city."
            )
        )

        // Sync to cloud if configured
        scope.launch(Dispatchers.IO) {
            supabaseClient.syncDonationToCloud(donation)
        }

        return Result.success(donation)
    }

    suspend fun updateDonationStatus(donationId: String, newStatus: String) {
        dao.updateDonationStatus(donationId, newStatus)
        val donation = dao.getDonationById(donationId)
        if (donation != null) {
            dao.insertNotification(
                AppNotification(
                    user_id = donation.donor_id,
                    title = "Donation Status: $newStatus",
                    message = "Your donation '${donation.food_name}' (${donation.donation_id}) is now $newStatus."
                )
            )
        }
    }

    suspend fun deleteDonation(donationId: String) {
        dao.deleteDonationById(donationId)
    }

    // --- Food Requests ---
    val allFoodRequests: Flow<List<FoodRequest>> = dao.getAllFoodRequestsFlow()

    fun getMyFoodRequests(userId: String): Flow<List<FoodRequest>> = dao.getFoodRequestsByRequesterFlow(userId)

    suspend fun createFoodRequest(
        category: String,
        quantity: String,
        peopleCount: Int,
        requiredDate: String,
        requiredTime: String,
        location: String,
        contactNumber: String,
        reason: String,
        notes: String
    ): Result<FoodRequest> {
        val user = _currentUser.value ?: return Result.failure(Exception("Please log in to submit a request"))

        val req = FoodRequest(
            requester_id = user.id,
            requester_name = user.full_name,
            category = category,
            quantity = quantity.trim(),
            people_count = peopleCount,
            required_date = requiredDate.trim(),
            required_time = requiredTime.trim(),
            location = location.trim(),
            contact_number = contactNumber.trim(),
            reason = reason.trim(),
            notes = notes.trim(),
            status = "Pending"
        )

        dao.insertFoodRequest(req)

        dao.insertNotification(
            AppNotification(
                user_id = user.id,
                title = "Food Request Submitted: ${req.request_id}",
                message = "Your request for $category food ($quantity for $peopleCount people) is pending review."
            )
        )

        scope.launch(Dispatchers.IO) {
            supabaseClient.syncFoodRequestToCloud(req)
        }

        return Result.success(req)
    }

    suspend fun updateFoodRequestStatus(requestId: String, newStatus: String) {
        dao.updateFoodRequestStatus(requestId, newStatus)
        val allReqs = dao.getAllFoodRequestsFlow().first()
        val match = allReqs.find { it.id == requestId }
        if (match != null) {
            dao.insertNotification(
                AppNotification(
                    user_id = match.requester_id,
                    title = "Request Status: $newStatus",
                    message = "Your food request ${match.request_id} has been marked as $newStatus."
                )
            )
        }
    }

    // --- Donation Request (Matching Food) ---
    suspend fun requestSpecificDonation(donation: FoodDonation): Result<DonationRequest> {
        val user = _currentUser.value ?: return Result.failure(Exception("Please log in to request this food"))

        val req = DonationRequest(
            donation_id = donation.id,
            requester_id = user.id,
            requester_name = user.full_name,
            status = "Pending"
        )
        dao.insertDonationRequest(req)

        // Notify donor
        dao.insertNotification(
            AppNotification(
                user_id = donation.donor_id,
                title = "New Request on ${donation.donation_id}",
                message = "${user.full_name} (${user.user_type}) requested your food donation '${donation.food_name}'."
            )
        )

        // Notify requester
        dao.insertNotification(
            AppNotification(
                user_id = user.id,
                title = "Request Sent for ${donation.food_name}",
                message = "Your request for donation ${donation.donation_id} was sent to the donor. Status: Pending."
            )
        )

        return Result.success(req)
    }

    // --- Notifications ---
    fun getMyNotifications(userId: String): Flow<List<AppNotification>> = dao.getNotificationsForUserFlow(userId)

    fun getUnreadCount(userId: String): Flow<Int> = dao.getUnreadCountFlow(userId)

    suspend fun markNotificationAsRead(id: String) = dao.markNotificationAsRead(id)

    suspend fun markAllNotificationsAsRead(userId: String) = dao.markAllNotificationsAsRead(userId)

    // --- Contact Messages ---
    val allContactMessages: Flow<List<ContactMessage>> = dao.getAllContactMessagesFlow()

    suspend fun submitContactMessage(name: String, email: String, phone: String, subject: String, message: String): Result<Unit> {
        val msg = ContactMessage(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            subject = subject.trim(),
            message = message.trim()
        )
        dao.insertContactMessage(msg)
        scope.launch(Dispatchers.IO) {
            supabaseClient.syncContactMessageToCloud(msg)
        }
        return Result.success(Unit)
    }

    // --- Admin Operations ---
    val allProfiles: Flow<List<UserProfile>> = dao.getAllProfilesFlow()

    suspend fun toggleUserStatus(userId: String, newStatus: String) {
        dao.updateUserStatus(userId, newStatus)
    }

    // --- Live Statistics ---
    val stats = allDonations.map { donations ->
        val totalDonations = donations.size
        val activeDonations = donations.count { it.status == "Active" || it.status == "Approved" }
        val completedDonations = donations.count { it.status == "Completed" }
        val mealsShared = donations.sumOf { it.people_served }
        LiveStats(
            totalDonations = totalDonations,
            activeDonations = activeDonations,
            completedDonations = completedDonations,
            mealsShared = mealsShared
        )
    }

    val totalUsersCount = allProfiles.map { it.size }
    val totalRequestsCount = allFoodRequests.map { it.size }
}

data class LiveStats(
    val totalDonations: Int = 0,
    val activeDonations: Int = 0,
    val completedDonations: Int = 0,
    val mealsShared: Int = 0
)
