package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.data.model.AppNotification
import com.example.data.model.FoodDonation
import com.example.data.model.FoodRequest
import com.example.data.model.UserProfile
import com.example.data.repository.LiveStats
import com.example.ui.components.StatCard
import com.example.ui.components.StatusBadge
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    currentUser: UserProfile?,
    liveStats: LiveStats,
    totalUsers: Int,
    totalRequests: Int,
    myDonations: List<FoodDonation>,
    myRequests: List<FoodRequest>,
    recentNotifications: List<AppNotification>,
    onNavigateToDonate: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToAvailable: () -> Unit,
    onNavigateToMyDonations: () -> Unit,
    onNavigateToMyRequests: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToWhatsAppHelp: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentUser != null) "Hello, ${currentUser.full_name} 👋" else "Welcome to MealBridge",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (currentUser != null) "Role: ${currentUser.user_type} • Status: ${currentUser.status}" else "Log in to track your donations and active food requests",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    if (currentUser == null) {
                        Button(
                            onClick = onNavigateToLogin,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Text("Log In")
                        }
                    } else if (currentUser.is_admin) {
                        FilledTonalButton(
                            onClick = onNavigateToAdmin,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = OrangeLight)
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = OrangeHover, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Admin", color = OrangeHover, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Live Platform Statistics (Mandatory: Total Donations, Active Donations, Total Requests, Completed Donations, Meals Shared)
        item {
            Column {
                Text(
                    text = "Live Database Metrics",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Total Donations",
                        value = "${liveStats.totalDonations}",
                        icon = Icons.Default.Fastfood,
                        iconTint = GreenPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Active Donations",
                        value = "${liveStats.activeDonations}",
                        icon = Icons.Default.CheckCircle,
                        iconTint = OrangeAccent,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Total Requests",
                        value = "$totalRequests",
                        icon = Icons.Default.Assignment,
                        iconTint = BlueAccent,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Completed",
                        value = "${liveStats.completedDonations}",
                        icon = Icons.Default.DoneAll,
                        iconTint = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                StatCard(
                    title = "Total Meals Shared to Community",
                    value = "${liveStats.mealsShared}+ Meals Fed",
                    icon = Icons.Default.Favorite,
                    iconTint = Color(0xFFE11D48),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Quick Actions
        item {
            Column {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = onNavigateToDonate,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, tint = GreenPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Donate", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    FilledTonalButton(
                        onClick = onNavigateToRequest,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.SoupKitchen, contentDescription = null, tint = OrangeAccent)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Request", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    FilledTonalButton(
                        onClick = onNavigateToAvailable,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = BlueAccent)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Browse", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    FilledTonalButton(
                        onClick = onNavigateToWhatsAppHelp,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = Color(0xFF25D366))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("WhatsApp", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }

        // My Donations Section (if logged in)
        if (currentUser != null) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "My Food Donations (${myDonations.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            TextButton(onClick = onNavigateToMyDonations) {
                                Text("View All")
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }

                        if (myDonations.isEmpty()) {
                            Text(
                                text = "You haven't listed any food donations yet.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            myDonations.take(3).forEach { donation ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(donation.food_name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                        Text("${donation.quantity} • ${donation.city}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                    }
                                    StatusBadge(status = donation.status)
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                            }
                        }
                    }
                }
            }

            // My Requests Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "My Food Requests (${myRequests.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            TextButton(onClick = onNavigateToMyRequests) {
                                Text("View All")
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }

                        if (myRequests.isEmpty()) {
                            Text(
                                text = "You have no active food requests.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            myRequests.take(3).forEach { req ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("${req.category} (${req.quantity})", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                        Text("For ${req.people_count} people in ${req.location}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                                    }
                                    StatusBadge(status = req.status)
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                            }
                        }
                    }
                }
            }
        }

        // WhatsApp Support Quick Connect Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("MealBridge WhatsApp Support", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = GreenDark))
                        Text("+91 7548813430 • Instant Volunteer Support", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                    WhatsAppActionButton(
                        text = "Chat",
                        onClick = onNavigateToWhatsAppHelp,
                        modifier = Modifier.height(40.dp)
                    )
                }
            }
        }
    }
}
