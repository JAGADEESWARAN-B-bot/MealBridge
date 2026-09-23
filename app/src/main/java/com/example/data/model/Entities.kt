package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "profiles")
data class UserProfile(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val full_name: String,
    val email: String,
    val phone: String,
    val user_type: String, // "Donor", "Food Seeker", "Volunteer", "Organization"
    val address: String,
    val status: String = "Active", // "Active", "Inactive"
    val is_admin: Boolean = false,
    val created_at: Long = System.currentTimeMillis()
)

@Entity(tableName = "donations")
data class FoodDonation(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val donation_id: String = "DON-${(1000..9999).random()}",
    val donor_id: String,
    val donor_name: String = "",
    val food_name: String,
    val category: String, // "Rice", "Biriyani", "Vegetable Food", "Snacks", "Fruits", "Bakery Items", "Packed Food", "Other"
    val quantity: String,
    val people_served: Int,
    val food_type: String, // "Vegetarian", "Non-Vegetarian"
    val preparation_date: String,
    val expiry_date: String,
    val pickup_address: String,
    val city: String,
    val contact_number: String,
    val description: String,
    val image_url: String = "",
    val status: String = "Active", // "Active", "Pending", "Approved", "Completed", "Rejected"
    val created_at: Long = System.currentTimeMillis()
)

@Entity(tableName = "food_requests")
data class FoodRequest(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val request_id: String = "REQ-${(1000..9999).random()}",
    val requester_id: String,
    val requester_name: String = "",
    val category: String,
    val quantity: String,
    val people_count: Int,
    val required_date: String,
    val required_time: String,
    val location: String,
    val contact_number: String,
    val reason: String,
    val notes: String = "",
    val status: String = "Pending", // "Pending", "Approved", "Completed", "Rejected"
    val created_at: Long = System.currentTimeMillis()
)

@Entity(tableName = "donation_requests")
data class DonationRequest(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val donation_id: String,
    val requester_id: String,
    val requester_name: String = "",
    val status: String = "Pending", // "Pending", "Accepted", "Rejected", "Completed"
    val created_at: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class AppNotification(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val user_id: String,
    val title: String,
    val message: String,
    val is_read: Boolean = false,
    val created_at: Long = System.currentTimeMillis()
)

@Entity(tableName = "contact_messages")
data class ContactMessage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val phone: String,
    val subject: String,
    val message: String,
    val created_at: Long = System.currentTimeMillis()
)
