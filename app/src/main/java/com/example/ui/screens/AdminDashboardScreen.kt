package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
import com.example.data.model.ContactMessage
import com.example.data.model.FoodDonation
import com.example.data.model.FoodRequest
import com.example.data.model.UserProfile
import com.example.data.repository.LiveStats
import com.example.ui.components.StatCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    liveStats: LiveStats,
    users: List<UserProfile>,
    donations: List<FoodDonation>,
    requests: List<FoodRequest>,
    contactMessages: List<ContactMessage>,
    onToggleUserStatus: (String, String) -> Unit,
    onUpdateDonationStatus: (String, String) -> Unit,
    onDeleteDonation: (String) -> Unit,
    onUpdateRequestStatus: (String, String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Donations (${donations.size})", "Requests (${requests.size})", "Users (${users.size})", "Messages (${contactMessages.size})")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_dashboard_screen")
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = OrangeAccent)
                    Text("MealBridge Admin Portal", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
                Text("Manage verified donations, incoming food requests, and registered community members.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))

                Spacer(modifier = Modifier.height(10.dp))

                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, fontSize = 12.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }
            }
        }

        when (selectedTab) {
            0 -> AdminOverviewTab(liveStats, users.size, requests.size)
            1 -> AdminDonationsTab(donations, onUpdateDonationStatus, onDeleteDonation)
            2 -> AdminRequestsTab(requests, onUpdateRequestStatus)
            3 -> AdminUsersTab(users, onToggleUserStatus)
            4 -> AdminMessagesTab(contactMessages)
        }
    }
}

@Composable
fun AdminOverviewTab(stats: LiveStats, totalUsers: Int, totalRequests: Int) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Live Platform Statistics (Supabase Database)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(title = "Total Users", value = "$totalUsers", icon = Icons.Default.People, iconTint = BlueAccent, modifier = Modifier.weight(1f))
                StatCard(title = "Total Donations", value = "${stats.totalDonations}", icon = Icons.Default.Fastfood, iconTint = GreenPrimary, modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(title = "Total Requests", value = "$totalRequests", icon = Icons.Default.Assignment, iconTint = OrangeAccent, modifier = Modifier.weight(1f))
                StatCard(title = "Active Donations", value = "${stats.activeDonations}", icon = Icons.Default.CheckCircle, iconTint = Color(0xFF10B981), modifier = Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(title = "Completed", value = "${stats.completedDonations}", icon = Icons.Default.DoneAll, iconTint = Color(0xFF3B82F6), modifier = Modifier.weight(1f))
                StatCard(title = "Meals Shared", value = "${stats.mealsShared}+", icon = Icons.Default.Favorite, iconTint = Color(0xFFE11D48), modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun AdminDonationsTab(
    donations: List<FoodDonation>,
    onUpdateStatus: (String, String) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(donations, key = { it.id }) { donation ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(donation.donation_id, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = GreenDark))
                        StatusBadge(status = donation.status)
                    }
                    Text(donation.food_name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text("Donor: ${donation.donor_name} • ${donation.quantity} (${donation.people_served} people) in ${donation.city}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { onUpdateStatus(donation.id, "Approved") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Approve", fontSize = 11.sp)
                        }

                        FilledTonalButton(
                            onClick = { onUpdateStatus(donation.id, "Completed") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Complete", fontSize = 11.sp)
                        }

                        FilledTonalButton(
                            onClick = { onUpdateStatus(donation.id, "Rejected") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFFFEE2E2))
                        ) {
                            Text("Reject", fontSize = 11.sp, color = Color(0xFFDC2626))
                        }

                        IconButton(
                            onClick = { onDelete(donation.id) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminRequestsTab(
    requests: List<FoodRequest>,
    onUpdateStatus: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(requests, key = { it.id }) { req ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(req.request_id, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = OrangeAccent))
                        StatusBadge(status = req.status)
                    }
                    Text("${req.category} Food Request", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text("Requester: ${req.requester_name} • ${req.quantity} for ${req.people_count} people in ${req.location}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Text("Reason: ${req.reason}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary), modifier = Modifier.padding(top = 2.dp))

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { onUpdateStatus(req.id, "Approved") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Text("Approve", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { onUpdateStatus(req.id, "Completed") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                        ) {
                            Text("Complete", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { onUpdateStatus(req.id, "Rejected") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                        ) {
                            Text("Reject", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminUsersTab(
    users: List<UserProfile>,
    onToggleUserStatus: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(users, key = { it.id }) { user ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(user.full_name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            if (user.is_admin) {
                                Surface(color = OrangeLight, shape = RoundedCornerShape(4.dp)) {
                                    Text("ADMIN", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OrangeHover, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                        }
                        Text("${user.email} • ${user.phone}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        Text("Type: ${user.user_type} • ${user.address}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }

                    OutlinedButton(
                        onClick = { onToggleUserStatus(user.id, user.status) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (user.status == "Active") Color(0xFFDC2626) else GreenDark
                        )
                    ) {
                        Text(if (user.status == "Active") "Deactivate" else "Activate", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMessagesTab(messages: List<ContactMessage>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(messages, key = { it.id }) { msg ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(msg.subject, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text("From: ${msg.name} (${msg.email}, ${msg.phone})", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(msg.message, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
