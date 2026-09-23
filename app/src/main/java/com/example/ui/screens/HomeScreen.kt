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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.LiveStats
import com.example.ui.components.StatCard
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    liveStats: LiveStats,
    totalUsers: Int,
    totalRequests: Int,
    onNavigateToDonate: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToAvailable: () -> Unit,
    onNavigateToWhatsApp: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF15803D),
                                    Color(0xFF16A34A),
                                    Color(0xFF047857)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = "🌱 Community Food Sharing Platform",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Text(
                            text = "Share Food.\nReduce Waste.\nHelp Someone Today.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 34.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "MealBridge connects surplus fresh food with people and shelters who need food, helping communities eliminate hunger and waste.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFDCFCE7),
                                lineHeight = 20.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onNavigateToDonate,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("home_donate_food_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = OrangeAccent,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Donate Food", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = onNavigateToRequest,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("home_request_food_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = GreenDark
                                )
                            ) {
                                Icon(Icons.Default.SoupKitchen, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Request Food", fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = onNavigateToAvailable,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("home_view_available_food_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color.White)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Browse Available Food Nearby", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Live Database Statistics Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF22C55E))
                    )
                    Text(
                        text = "LIVE PLATFORM IMPACT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GreenDark,
                            letterSpacing = 1.sp
                        )
                    )
                }
                Text(
                    text = "Real-Time Community Statistics",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Total Users",
                        value = "$totalUsers",
                        icon = Icons.Default.People,
                        iconTint = BlueAccent,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Food Donations",
                        value = "${liveStats.totalDonations}",
                        icon = Icons.Default.Fastfood,
                        iconTint = GreenPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Food Requests",
                        value = "$totalRequests",
                        icon = Icons.Default.Assignment,
                        iconTint = OrangeAccent,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Meals Shared",
                        value = "${liveStats.mealsShared}+",
                        icon = Icons.Default.Favorite,
                        iconTint = Color(0xFFE11D48),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // How MealBridge Works
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "How MealBridge Works",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Four simple steps to bridge surplus food with community members.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Spacer(modifier = Modifier.height(14.dp))

                val steps = listOf(
                    Triple("1. Register", "Create your account as Donor, Seeker, Volunteer, or Organization.", Icons.Default.PersonAdd),
                    Triple("2. Donate or Request", "List surplus fresh meals or post an urgent food requirement.", Icons.Default.PostAdd),
                    Triple("3. Connect", "Direct WhatsApp notifications & smart matching alert local shelters.", Icons.Default.ShareLocation),
                    Triple("4. Share Food", "Coordinate pickup effortlessly and confirm delivery.", Icons.Default.DoneAll)
                )

                steps.forEachIndexed { index, (stepTitle, stepDesc, stepIcon) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(GreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(stepIcon, contentDescription = null, tint = GreenDark, modifier = Modifier.size(22.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(stepTitle, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Text(stepDesc, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                            }
                        }
                    }
                }
            }
        }

        // Features Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Key Features",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))

                val features = listOf(
                    Pair("Food Donation", "Easily list surplus home, wedding, or restaurant meals with preparation and expiry timestamps."),
                    Pair("Food Requests", "Shelters, NGOs, and seekers request specific food quantities with pickup timing."),
                    Pair("Available Food", "Search and filter listings by category, vegetarian/non-vegetarian, and city area."),
                    Pair("Smart Matching", "Instant pairing between nearby donors and active food requests."),
                    Pair("Donation Tracking", "Real-time statuses: Pending, Approved, Completed, and notification alerts."),
                    Pair("WhatsApp Communication", "Pre-filled messages and direct chat with donors, seekers, and MealBridge support."),
                    Pair("Secure Accounts", "Role-based accounts for Donors, Seekers, Volunteers, and Admins.")
                )

                features.forEach { (title, desc) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GreenPrimary,
                            modifier = Modifier.size(20.dp).padding(top = 2.dp)
                        )
                        Column {
                            Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(desc, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                    }
                }
            }
        }

        // WhatsApp Help Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = GreenDark,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Need Immediate Assistance?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Our community coordinators are active 24/7 on WhatsApp (+91 7548813430) to facilitate emergency food deliveries.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    WhatsAppActionButton(
                        text = "Chat on WhatsApp (+91 7548813430)",
                        onClick = onNavigateToWhatsApp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
