package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AppNotification
import com.example.data.model.ContactMessage
import com.example.data.model.DonationRequest
import com.example.data.model.FoodDonation
import com.example.data.model.FoodRequest
import com.example.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        FoodDonation::class,
        FoodRequest::class,
        DonationRequest::class,
        AppNotification::class,
        ContactMessage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MealBridgeDatabase : RoomDatabase() {
    abstract fun dao(): MealBridgeDao

    companion object {
        @Volatile
        private var INSTANCE: MealBridgeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MealBridgeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealBridgeDatabase::class.java,
                    "mealbridge_community.db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialCommunityData(database.dao())
                    }
                }
            }
        }

        suspend fun populateInitialCommunityData(dao: MealBridgeDao) {
            val adminUser = UserProfile(
                id = "admin-user-001",
                full_name = "MealBridge Admin",
                email = "admin@mealbridge.org",
                phone = "+91 7548813430",
                user_type = "Organization",
                address = "Community Center, Tech Park, Chennai",
                status = "Active",
                is_admin = true
            )
            val demoDonor = UserProfile(
                id = "donor-user-101",
                full_name = "Green Leaf Catering",
                email = "donor@mealbridge.org",
                phone = "+91 9840123456",
                user_type = "Donor",
                address = "12 North Mada St, Mylapore, Chennai",
                status = "Active",
                is_admin = false
            )
            val demoSeeker = UserProfile(
                id = "seeker-user-201",
                full_name = "Anbu Ashram Children Home",
                email = "seeker@mealbridge.org",
                phone = "+91 9789012345",
                user_type = "Food Seeker",
                address = "45 Gandhi Road, Tambaram, Chennai",
                status = "Active",
                is_admin = false
            )

            dao.insertProfile(adminUser)
            dao.insertProfile(demoDonor)
            dao.insertProfile(demoSeeker)

            // Seed community donations
            val donation1 = FoodDonation(
                id = "don-001",
                donation_id = "DON-1021",
                donor_id = demoDonor.id,
                donor_name = demoDonor.full_name,
                food_name = "Fresh Veg Biriyani with Raitha",
                category = "Biriyani",
                quantity = "15 kg",
                people_served = 45,
                food_type = "Vegetarian",
                preparation_date = "Today, 1:00 PM",
                expiry_date = "Tonight, 10:00 PM",
                pickup_address = "Mylapore Hall, 12 North Mada St",
                city = "Chennai",
                contact_number = "+91 9840123456",
                description = "Surplus fresh wedding feast vegetable biriyani prepared by professional chefs. Hygenically packed in food-grade trays.",
                image_url = "",
                status = "Active"
            )
            val donation2 = FoodDonation(
                id = "don-002",
                donation_id = "DON-1022",
                donor_id = demoDonor.id,
                donor_name = "Annapoorna Kitchen",
                food_name = "Steamed Rice, Sambar & Poriyal",
                category = "Rice",
                quantity = "25 Meals",
                people_served = 25,
                food_type = "Vegetarian",
                preparation_date = "Today, 12:30 PM",
                expiry_date = "Tonight, 9:00 PM",
                pickup_address = "RS Puram West",
                city = "Coimbatore",
                contact_number = "+91 9443219876",
                description = "Hot freshly prepared South Indian lunch combo with aromatic drumstick sambar and cabbage carrot poriyal.",
                image_url = "",
                status = "Active"
            )
            val donation3 = FoodDonation(
                id = "don-003",
                donation_id = "DON-1023",
                donor_id = demoDonor.id,
                donor_name = "Bake & Joy Bakery",
                food_name = "Assorted Wheat Bread & Veg Puffs",
                category = "Bakery Items",
                quantity = "40 Pieces",
                people_served = 30,
                food_type = "Vegetarian",
                preparation_date = "This Morning, 8:00 AM",
                expiry_date = "Tomorrow, 6:00 PM",
                pickup_address = "Koramangala 4th Block",
                city = "Bangalore",
                contact_number = "+91 9880192837",
                description = "Freshly baked whole wheat loaves, buns, and warm spiced vegetable puffs from afternoon bakery batch.",
                image_url = "",
                status = "Active"
            )
            val donation4 = FoodDonation(
                id = "don-004",
                donation_id = "DON-1024",
                donor_id = demoDonor.id,
                donor_name = "Orchard Fresh Supply",
                food_name = "Fresh Bananas, Apples & Oranges Basket",
                category = "Fruits",
                quantity = "18 kg",
                people_served = 50,
                food_type = "Vegetarian",
                preparation_date = "Today Morning",
                expiry_date = "In 3 Days",
                pickup_address = "Koyambedu Wholesale Market",
                city = "Chennai",
                contact_number = "+91 9841098765",
                description = "Healthy, fresh unblemished fruit crate suitable for elderly homes or shelter centers.",
                image_url = "",
                status = "Active"
            )

            dao.insertDonation(donation1)
            dao.insertDonation(donation2)
            dao.insertDonation(donation3)
            dao.insertDonation(donation4)

            // Seed community food requests
            val request1 = FoodRequest(
                id = "req-001",
                request_id = "REQ-2031",
                requester_id = demoSeeker.id,
                requester_name = demoSeeker.full_name,
                category = "Rice",
                quantity = "30 Meals",
                people_count = 30,
                required_date = "Today",
                required_time = "7:30 PM",
                location = "Tambaram Sanatorium",
                contact_number = "+91 9789012345",
                reason = "Evening dinner for resident children and volunteers.",
                notes = "Vegetarian meal preferred.",
                status = "Pending"
            )
            val request2 = FoodRequest(
                id = "req-002",
                request_id = "REQ-2032",
                requester_id = demoSeeker.id,
                requester_name = "Sneha Old Age Care",
                category = "Vegetable Food",
                quantity = "20 Packs",
                people_count = 20,
                required_date = "Tomorrow",
                required_time = "1:00 PM",
                location = "Velachery Bypass Road",
                contact_number = "+91 9840912834",
                reason = "Lunch supply for senior citizens.",
                notes = "Low spice food requested.",
                status = "Approved"
            )

            dao.insertFoodRequest(request1)
            dao.insertFoodRequest(request2)

            // Seed welcome notification
            dao.insertNotification(
                AppNotification(
                    id = "notif-001",
                    user_id = demoDonor.id,
                    title = "Welcome to MealBridge!",
                    message = "Thank you for joining our community mission to eliminate food waste and feed those in need.",
                    is_read = false
                )
            )
            dao.insertNotification(
                AppNotification(
                    id = "notif-002",
                    user_id = demoDonor.id,
                    title = "Donation Listed: DON-1021",
                    message = "Your Veg Biriyani donation is now live and visible to food seekers in Chennai.",
                    is_read = false
                )
            )
        }
    }
}
