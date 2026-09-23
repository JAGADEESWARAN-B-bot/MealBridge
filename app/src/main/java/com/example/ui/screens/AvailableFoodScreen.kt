package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.components.FoodTypeBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.*

@Composable
fun AvailableFoodScreen(
    donations: List<FoodDonation>,
    onRequestFood: (FoodDonation) -> Unit,
    onShareOnWhatsApp: (FoodDonation) -> Unit,
    onSelectDonation: (FoodDonation) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedFoodType by remember { mutableStateOf("All") }

    val categories = listOf("All", "Rice", "Biriyani", "Vegetable Food", "Snacks", "Fruits", "Bakery Items", "Packed Food", "Other")

    val filteredDonations = remember(donations, searchQuery, selectedCategory, selectedFoodType) {
        donations.filter { donation ->
            val matchesSearch = donation.food_name.contains(searchQuery, ignoreCase = true) ||
                    donation.city.contains(searchQuery, ignoreCase = true) ||
                    donation.category.contains(searchQuery, ignoreCase = true) ||
                    donation.pickup_address.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "All" || donation.category.equals(selectedCategory, ignoreCase = true)
            val matchesType = selectedFoodType == "All" || donation.food_type.equals(selectedFoodType, ignoreCase = true)
            matchesSearch && matchesCategory && matchesType
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("available_food_screen")
    ) {
        // Search & Filter Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search food name, area, or category...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("available_food_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Veg / Non-Veg / All toggle chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFoodType == "All",
                        onClick = { selectedFoodType = "All" },
                        label = { Text("All Types") }
                    )
                    FilterChip(
                        selected = selectedFoodType == "Vegetarian",
                        onClick = { selectedFoodType = "Vegetarian" },
                        label = { Text("Vegetarian") },
                        leadingIcon = {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF16A34A)))
                        }
                    )
                    FilterChip(
                        selected = selectedFoodType == "Non-Vegetarian",
                        onClick = { selectedFoodType = "Non-Vegetarian" },
                        label = { Text("Non-Veg") },
                        leadingIcon = {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFDC2626)))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Horizontal Category filter chips
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
                            label = { Text(cat, fontSize = 12.sp) }
                        )
                    }
                }
            }
        }

        // List of Donations
        if (filteredDonations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Available Food Found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Try adjusting your search keywords or category filters.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredDonations, key = { it.id }) { donation ->
                    DonationCard(
                        donation = donation,
                        onCardClick = { onSelectDonation(donation) },
                        onRequestClick = { onRequestFood(donation) },
                        onShareWhatsAppClick = { onShareOnWhatsApp(donation) }
                    )
                }
            }
        }
    }
}

@Composable
fun DonationCard(
    donation: FoodDonation,
    onCardClick: () -> Unit,
    onRequestClick: () -> Unit,
    onShareWhatsAppClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
            .testTag("donation_card_${donation.donation_id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Category, Veg indicator, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FoodTypeBadge(isVeg = donation.food_type.equals("Vegetarian", ignoreCase = true))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = donation.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                StatusBadge(status = donation.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Food Name & ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = donation.food_name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "ID: ${donation.donation_id} • Donor: ${donation.donor_name}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details Row: Quantity, People Served, Expiry
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Quantity", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    Text(donation.quantity, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                }
                Column {
                    Text("People Served", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    Text("${donation.people_served} People", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = GreenDark))
                }
                Column {
                    Text("Expires", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    Text(donation.expiry_date, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = OrangeAccent))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(16.dp))
                Text(
                    text = "${donation.pickup_address}, ${donation.city}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRequestClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("request_food_card_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Request This Food", fontWeight = FontWeight.Bold)
                }

                WhatsAppActionButton(
                    text = "Share",
                    onClick = onShareWhatsAppClick,
                    modifier = Modifier.weight(0.7f),
                    isOutlined = true
                )
            }
        }
    }
}
