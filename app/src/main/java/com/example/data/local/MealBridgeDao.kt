package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.data.model.AppNotification
import com.example.data.model.ContactMessage
import com.example.data.model.DonationRequest
import com.example.data.model.FoodDonation
import com.example.data.model.FoodRequest
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface MealBridgeDao {

    // --- Profiles ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    fun getProfileByIdFlow(id: String): Flow<UserProfile?>

    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: String): UserProfile?

    @Query("SELECT * FROM profiles WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getProfileByEmail(email: String): UserProfile?

    @Query("SELECT * FROM profiles ORDER BY created_at DESC")
    fun getAllProfilesFlow(): Flow<List<UserProfile>>

    @Query("UPDATE profiles SET status = :status WHERE id = :id")
    suspend fun updateUserStatus(id: String, status: String)

    // --- Food Donations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonation(donation: FoodDonation)

    @Update
    suspend fun updateDonation(donation: FoodDonation)

    @Query("DELETE FROM donations WHERE id = :id")
    suspend fun deleteDonationById(id: String)

    @Query("SELECT * FROM donations ORDER BY created_at DESC")
    fun getAllDonationsFlow(): Flow<List<FoodDonation>>

    @Query("SELECT * FROM donations WHERE status IN ('Active', 'Approved') ORDER BY created_at DESC")
    fun getAvailableDonationsFlow(): Flow<List<FoodDonation>>

    @Query("SELECT * FROM donations WHERE donor_id = :donorId ORDER BY created_at DESC")
    fun getDonationsByDonorFlow(donorId: String): Flow<List<FoodDonation>>

    @Query("SELECT * FROM donations WHERE id = :id LIMIT 1")
    fun getDonationByIdFlow(id: String): Flow<FoodDonation?>

    @Query("SELECT * FROM donations WHERE id = :id LIMIT 1")
    suspend fun getDonationById(id: String): FoodDonation?

    @Query("UPDATE donations SET status = :status WHERE id = :id")
    suspend fun updateDonationStatus(id: String, status: String)

    // --- Food Requests ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodRequest(request: FoodRequest)

    @Update
    suspend fun updateFoodRequest(request: FoodRequest)

    @Query("DELETE FROM food_requests WHERE id = :id")
    suspend fun deleteFoodRequestById(id: String)

    @Query("SELECT * FROM food_requests ORDER BY created_at DESC")
    fun getAllFoodRequestsFlow(): Flow<List<FoodRequest>>

    @Query("SELECT * FROM food_requests WHERE requester_id = :requesterId ORDER BY created_at DESC")
    fun getFoodRequestsByRequesterFlow(requesterId: String): Flow<List<FoodRequest>>

    @Query("SELECT * FROM food_requests WHERE id = :id LIMIT 1")
    fun getFoodRequestByIdFlow(id: String): Flow<FoodRequest?>

    @Query("UPDATE food_requests SET status = :status WHERE id = :id")
    suspend fun updateFoodRequestStatus(id: String, status: String)

    // --- Donation Requests (Matches) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonationRequest(request: DonationRequest)

    @Query("SELECT * FROM donation_requests WHERE donation_id = :donationId ORDER BY created_at DESC")
    fun getDonationRequestsByDonationFlow(donationId: String): Flow<List<DonationRequest>>

    @Query("SELECT * FROM donation_requests WHERE requester_id = :requesterId ORDER BY created_at DESC")
    fun getDonationRequestsByRequesterFlow(requesterId: String): Flow<List<DonationRequest>>

    @Query("UPDATE donation_requests SET status = :status WHERE id = :id")
    suspend fun updateDonationRequestStatus(id: String, status: String)

    // --- Notifications ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotification)

    @Query("SELECT * FROM notifications WHERE user_id = :userId ORDER BY created_at DESC")
    fun getNotificationsForUserFlow(userId: String): Flow<List<AppNotification>>

    @Query("SELECT COUNT(*) FROM notifications WHERE user_id = :userId AND is_read = 0")
    fun getUnreadCountFlow(userId: String): Flow<Int>

    @Query("UPDATE notifications SET is_read = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: String)

    @Query("UPDATE notifications SET is_read = 1 WHERE user_id = :userId")
    suspend fun markAllNotificationsAsRead(userId: String)

    // --- Contact Messages ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactMessage(message: ContactMessage)

    @Query("SELECT * FROM contact_messages ORDER BY created_at DESC")
    fun getAllContactMessagesFlow(): Flow<List<ContactMessage>>
}
