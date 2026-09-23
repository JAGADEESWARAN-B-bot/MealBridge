package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodDonation
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.*

@Composable
fun DonateFoodScreen(
    isLoggedIn: Boolean,
    onNavigateToLogin: () -> Unit,
    onSubmitDonation: (
        foodName: String,
        category: String,
        quantity: String,
        peopleServed: Int,
        foodType: String,
        prepDate: String,
        expiryDate: String,
        pickupAddress: String,
        city: String,
        contact: String,
        description: String,
        imageUrl: String,
        onSuccess: (FoodDonation) -> Unit,
        onError: (String) -> Unit
    ) -> Unit,
    onShareOnWhatsApp: (FoodDonation) -> Unit,
    onChatOnWhatsApp: (FoodDonation) -> Unit
) {
    if (!isLoggedIn) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = OrangeAccent,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Authentication Required",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please log in or create an account to list surplus food donations. Your donations are securely tracked and linked to your profile.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("donate_screen_login_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                    ) {
                        Text("Log In / Register", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    var foodName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Rice") }
    var quantity by remember { mutableStateOf("") }
    var peopleServed by remember { mutableStateOf("25") }
    var foodType by remember { mutableStateOf("Vegetarian") }
    var prepDate by remember { mutableStateOf("Today, 1:00 PM") }
    var expiryDate by remember { mutableStateOf("Tonight, 10:00 PM") }
    var pickupAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Chennai") }
    var contactNumber by remember { mutableStateOf("+91 9840123456") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var submittedDonation by remember { mutableStateOf<FoodDonation?>(null) }

    val categories = listOf("Rice", "Biriyani", "Vegetable Food", "Snacks", "Fruits", "Bakery Items", "Packed Food", "Other")

    // Show Success Dialog after donation submission
    submittedDonation?.let { donation ->
        AlertDialog(
            onDismissRequest = { submittedDonation = null },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Donation Submitted Successfully!",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Donation ID: ${donation.donation_id}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = GreenDark
                        )
                    )
                    Text(
                        text = "Food: ${donation.food_name} (${donation.quantity})",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Your food donation is permanently stored and visible to local food seekers.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // WhatsApp actions as explicitly requested
                    WhatsAppActionButton(
                        text = "Share Donation on WhatsApp",
                        onClick = { onShareOnWhatsApp(donation) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    WhatsAppActionButton(
                        text = "Chat on WhatsApp (+91 7548813430)",
                        onClick = { onChatOnWhatsApp(donation) },
                        modifier = Modifier.fillMaxWidth(),
                        isOutlined = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        submittedDonation = null
                        // Clear form
                        foodName = ""
                        quantity = ""
                        description = ""
                        pickupAddress = ""
                    }
                ) {
                    Text("Done")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("donate_food_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Donate Surplus Food",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Share surplus food from weddings, functions, hotels, or homes with local shelters.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        }

        if (errorMessage != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = Color(0xFFDC2626))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(errorMessage ?: "", color = Color(0xFF991B1B), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        // Food Name
        item {
            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food Name *") },
                placeholder = { Text("e.g. Fresh Vegetable Biriyani, Meals Box") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("donate_food_name_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // Food Type (Veg / Non-Veg)
        item {
            Column {
                Text("Food Type *", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = foodType == "Vegetarian",
                        onClick = { foodType = "Vegetarian" },
                        label = { Text("Vegetarian (Veg)") },
                        leadingIcon = {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF16A34A)))
                        }
                    )
                    FilterChip(
                        selected = foodType == "Non-Vegetarian",
                        onClick = { foodType = "Non-Vegetarian" },
                        label = { Text("Non-Vegetarian (Non-Veg)") },
                        leadingIcon = {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFDC2626)))
                        }
                    )
                }
            }
        }

        // Category Selection
        item {
            Column {
                Text("Category *", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        }

        // Quantity & People Served
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity *") },
                    placeholder = { Text("e.g. 15 kg, 30 Packs") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("donate_quantity_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = peopleServed,
                    onValueChange = { peopleServed = it },
                    label = { Text("People Served *") },
                    placeholder = { Text("e.g. 35") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("donate_people_served_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // Preparation Date & Expiry Date
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = prepDate,
                    onValueChange = { prepDate = it },
                    label = { Text("Prepared Time *") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("donate_prep_date_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("Expiry Time *") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("donate_expiry_date_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // Pickup Address & City / Area
        item {
            OutlinedTextField(
                value = pickupAddress,
                onValueChange = { pickupAddress = it },
                label = { Text("Pickup Address *") },
                placeholder = { Text("Building, Street, Landmark") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("donate_pickup_address_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City / Area *") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("donate_city_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    label = { Text("Contact Number *") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("donate_contact_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // Description
        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description & Handling Notes") },
                placeholder = { Text("Mention dietary info, packaging type, storage conditions...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .testTag("donate_description_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )
        }

        // Submit Button
        item {
            Button(
                onClick = {
                    if (foodName.isBlank() || quantity.isBlank() || pickupAddress.isBlank()) {
                        errorMessage = "Please fill in all required fields (Food Name, Quantity, Pickup Address)."
                        return@Button
                    }
                    val people = peopleServed.toIntOrNull() ?: 10
                    errorMessage = null
                    isSubmitting = true
                    onSubmitDonation(
                        foodName,
                        selectedCategory,
                        quantity,
                        people,
                        foodType,
                        prepDate,
                        expiryDate,
                        pickupAddress,
                        city,
                        contactNumber,
                        description,
                        imageUrl,
                        { donation ->
                            isSubmitting = false
                            submittedDonation = donation
                        },
                        { err ->
                            isSubmitting = false
                            errorMessage = err
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_donation_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.CloudUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Food Donation", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
