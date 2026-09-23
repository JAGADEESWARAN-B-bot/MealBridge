package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.FoodRequest
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.*

@Composable
fun RequestFoodScreen(
    isLoggedIn: Boolean,
    onNavigateToLogin: () -> Unit,
    onSubmitRequest: (
        category: String,
        quantity: String,
        peopleCount: Int,
        requiredDate: String,
        requiredTime: String,
        location: String,
        contact: String,
        reason: String,
        notes: String,
        onSuccess: (FoodRequest) -> Unit,
        onError: (String) -> Unit
    ) -> Unit,
    onShareOnWhatsApp: (FoodRequest) -> Unit,
    onChatOnWhatsApp: (FoodRequest) -> Unit
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
                        text = "Please log in or register to submit a community food request. Food requests are broadcast to active local donors and verified volunteers.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("request_screen_login_button"),
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

    var selectedCategory by remember { mutableStateOf("Rice") }
    var quantityRequired by remember { mutableStateOf("") }
    var numberOfPeople by remember { mutableStateOf("30") }
    var requiredDate by remember { mutableStateOf("Today") }
    var requiredTime by remember { mutableStateOf("7:00 PM") }
    var location by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("+91 9789012345") }
    var reason by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var submittedRequest by remember { mutableStateOf<FoodRequest?>(null) }

    val categories = listOf("Rice", "Biriyani", "Vegetable Food", "Snacks", "Fruits", "Bakery Items", "Packed Food", "Other")

    submittedRequest?.let { req ->
        AlertDialog(
            onDismissRequest = { submittedRequest = null },
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
                    text = "Food Request Submitted!",
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
                        text = "Request ID: ${req.request_id}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = OrangeAccent
                        )
                    )
                    Text(
                        text = "Food Needed: ${req.category} (${req.quantity})",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Your request has been broadcast. Local donors and coordinators can see your need.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    WhatsAppActionButton(
                        text = "Share Request on WhatsApp",
                        onClick = { onShareOnWhatsApp(req) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    WhatsAppActionButton(
                        text = "Chat on WhatsApp (+91 7548813430)",
                        onClick = { onChatOnWhatsApp(req) },
                        modifier = Modifier.fillMaxWidth(),
                        isOutlined = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        submittedRequest = null
                        quantityRequired = ""
                        reason = ""
                        location = ""
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
            .testTag("request_food_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Request Community Food",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Request food support for orphanages, shelters, old age homes, or community members in need.",
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

        // Category Selection
        item {
            Column {
                Text("Food Category Needed *", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
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

        // Quantity & People Count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = quantityRequired,
                    onValueChange = { quantityRequired = it },
                    label = { Text("Quantity Needed *") },
                    placeholder = { Text("e.g. 20 kg or 35 Meals") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("request_quantity_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = numberOfPeople,
                    onValueChange = { numberOfPeople = it },
                    label = { Text("People to Feed *") },
                    placeholder = { Text("e.g. 30") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("request_people_count_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // Date & Time
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = requiredDate,
                    onValueChange = { requiredDate = it },
                    label = { Text("Required Date *") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("request_date_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = requiredTime,
                    onValueChange = { requiredTime = it },
                    label = { Text("Required Time *") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("request_time_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        // Location & Contact
        item {
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Delivery / Pickup Location *") },
                placeholder = { Text("Area, Landmark, Street Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("request_location_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it },
                label = { Text("Contact Number *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("request_contact_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // Reason
        item {
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Purpose / Reason *") },
                placeholder = { Text("e.g. Evening meal for resident children at shelter, flood relief...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .testTag("request_reason_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )
        }

        // Notes
        item {
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Additional Dietary Notes") },
                placeholder = { Text("Special requirements: low spice, diabetic-friendly, packing containers...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .testTag("request_notes_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )
        }

        // Submit Button
        item {
            Button(
                onClick = {
                    if (quantityRequired.isBlank() || location.isBlank() || reason.isBlank()) {
                        errorMessage = "Please fill in all required fields (Quantity, Location, Purpose)."
                        return@Button
                    }
                    val people = numberOfPeople.toIntOrNull() ?: 20
                    errorMessage = null
                    isSubmitting = true
                    onSubmitRequest(
                        selectedCategory,
                        quantityRequired,
                        people,
                        requiredDate,
                        requiredTime,
                        location,
                        contactNumber,
                        reason,
                        notes,
                        { req ->
                            isSubmitting = false
                            submittedRequest = req
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
                    .testTag("submit_food_request_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Food Request", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
