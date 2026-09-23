package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.WhatsAppActionButton
import com.example.ui.theme.*

@Composable
fun AboutScreen(onChatOnWhatsApp: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("about_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(GreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = GreenDark, modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("MealBridge", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = GreenDark))
                    Text("Community Food Sharing Platform", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(color = OrangeLight, shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = "\"Share Food. Reduce Waste. Help Someone Today.\"",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = OrangeHover),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Academic Project Context", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "This application is developed as a final-year B.Sc Computer Science (AI & Data Science) capstone project. It implements a full-stack architecture with permanent PostgreSQL/Room persistence, role-based workflows, smart donor-recipient pairing, and WhatsApp coordination.",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp, color = TextPrimary)
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Core Objectives & SDGs", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))

                    val goals = listOf(
                        Pair("Zero Hunger (SDG 2)", "Redirect nutritious surplus food from weddings, caterers, and bakeries to orphanages and daily-wage shelters."),
                        Pair("Responsible Consumption (SDG 12)", "Prevent edible food from ending up in municipal landfills and generating harmful methane emissions."),
                        Pair("Community Empowerment", "Equip volunteers and local non-profits with digital dispatch and instant WhatsApp communication channels.")
                    )

                    goals.forEach { (title, desc) ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(18.dp))
                            Column {
                                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text(desc, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                            }
                        }
                    }
                }
            }
        }

        item {
            WhatsAppActionButton(
                text = "Connect with Project Coordinator (+91 7548813430)",
                onClick = { onChatOnWhatsApp("Hello MealBridge Team! I would like to learn more about the MealBridge Community Food Sharing Platform.") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ContactScreen(
    onSubmitMessage: (name: String, email: String, phone: String, subject: String, message: String, onResult: (Boolean) -> Unit) -> Unit,
    onChatOnWhatsApp: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var isSubmitting by remember { mutableStateOf(false) }
    var successMsg by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("contact_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Contact MealBridge", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
            Text("Reach our volunteer team or share feedback with the platform administrators.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
        }

        if (successMsg) {
            item {
                Surface(color = GreenLight, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("Your message has been permanently recorded in the database. Our team will contact you shortly!", color = GreenDark, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
                }
            }
        }

        if (errorMsg != null) {
            item {
                Surface(color = Color(0xFFFEE2E2), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(errorMsg ?: "", color = Color(0xFF991B1B), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
                }
            }
        }

        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("contact_name_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("contact_email_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("contact_phone_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("contact_subject_input"),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message *") },
                modifier = Modifier.fillMaxWidth().height(100.dp).testTag("contact_message_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )
        }

        item {
            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || subject.isBlank() || message.isBlank()) {
                        errorMsg = "Please fill in all mandatory fields."
                        return@Button
                    }
                    errorMsg = null
                    isSubmitting = true
                    onSubmitMessage(name, email, phone, subject, message) { success ->
                        isSubmitting = false
                        if (success) {
                            successMsg = true
                            name = ""
                            email = ""
                            phone = ""
                            subject = ""
                            message = ""
                        } else {
                            errorMsg = "Failed to submit message."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("contact_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(12.dp),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                } else {
                    Text("Send Message", fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Instant WhatsApp Support", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GreenDark))
                    Text("Official Support Number: +91 7548813430", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(10.dp))
                    WhatsAppActionButton(
                        text = "Chat Directly on WhatsApp",
                        onClick = { onChatOnWhatsApp("Hello MealBridge Support! I have an urgent query regarding food sharing.") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun WhatsAppSectionScreen(onSendWhatsApp: (message: String) -> Unit) {
    var customMessage by remember { mutableStateOf("") }

    val prefilledTemplates = listOf(
        Pair("General Support", "Hello MealBridge! I would like more information on donating surplus food in my area."),
        Pair("Urgent Food Rescue", "URGENT FOOD RESCUE: We have surplus fresh meals available for immediate pickup. Please connect with our location coordinator."),
        Pair("Volunteer Sign Up", "Hello MealBridge Team! I would like to volunteer for weekend food collection and distribution in my city."),
        Pair("Shelter Food Request", "Hello MealBridge! Our shelter urgently requires dinner packages for 40 individuals tonight.")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("whatsapp_hub_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = null,
                        tint = Color(0xFF25D366),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("WhatsApp Community Hub", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GreenDark))
                    Text("Direct Support Line: +91 7548813430", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF128C7E)))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "MealBridge integrates official WhatsApp communication to enable real-time coordination between food donors, volunteer drivers, and community shelters.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    )
                }
            }
        }

        item {
            Text("Quick Action Templates", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }

        prefilledTemplates.forEach { (label, text) ->
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(label, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = GreenDark))
                        Text(text, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary), modifier = Modifier.padding(vertical = 4.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        WhatsAppActionButton(
                            text = "Send this on WhatsApp",
                            onClick = { onSendWhatsApp(text) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Custom WhatsApp Message", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = customMessage,
                        onValueChange = { customMessage = it },
                        placeholder = { Text("Type your message here...") },
                        modifier = Modifier.fillMaxWidth().height(90.dp),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    WhatsAppActionButton(
                        text = "Launch WhatsApp to +91 7548813430",
                        onClick = {
                            val msg = if (customMessage.isNotBlank()) customMessage else "Hello MealBridge Support!"
                            onSendWhatsApp(msg)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
