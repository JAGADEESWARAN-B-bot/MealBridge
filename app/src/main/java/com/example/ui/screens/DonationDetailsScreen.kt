package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodDonation
import com.example.ui.components.FoodTypeBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.*

@Composable
fun DonationDetailsScreen(
    donation: FoodDonation?,
    onBackClick: () -> Unit,
    onRequestFood: (FoodDonation) -> Unit,
    onShareOnWhatsApp: (FoodDonation) -> Unit,
    onChatOnWhatsApp: (FoodDonation) -> Unit
) {
    if (donation == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No donation selected.")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("donation_details_screen")
    ) {
        // App bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Donation Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = donation.donation_id,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GreenDark
                    ),
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FoodTypeBadge(isVeg = donation.food_type.equals("Vegetarian", ignoreCase = true))
                        StatusBadge(status = donation.status)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = donation.food_name,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "Category: ${donation.category} • Listed by ${donation.donor_name}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quantity & Servings Highlight
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = GreenLight,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Quantity", style = MaterialTheme.typography.labelSmall.copy(color = GreenDark))
                                Text(donation.quantity, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenDark))
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            color = OrangeLight,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("People Served", style = MaterialTheme.typography.labelSmall.copy(color = OrangeHover))
                                Text("${donation.people_served} Individuals", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = OrangeHover))
                            }
                        }
                    }
                }
            }

            // Expiry & Timings
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Timestamps & Freshness", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Prepared At:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text(donation.preparation_date, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Safe Consumption Expiry:", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text(donation.expiry_date, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = OrangeAccent))
                    }
                }
            }

            // Pickup Location
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = OrangeAccent)
                        Text("Pickup Address", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${donation.pickup_address}, ${donation.city}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Description
            if (donation.description.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Food Notes & Packaging", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(donation.description, style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp, color = TextPrimary))
                    }
                }
            }

            // Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onRequestFood(donation) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("details_request_food_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(Icons.Default.Done, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Request This Food Donation", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }

                WhatsAppActionButton(
                    text = "Share Donation on WhatsApp",
                    onClick = { onShareOnWhatsApp(donation) },
                    modifier = Modifier.fillMaxWidth(),
                    isOutlined = true
                )

                WhatsAppActionButton(
                    text = "Chat with MealBridge on WhatsApp",
                    onClick = { onChatOnWhatsApp(donation) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
