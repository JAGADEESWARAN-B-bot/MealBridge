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
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    currentUser: UserProfile?,
    onUpdateProfile: (name: String, phone: String, address: String, onResult: (Boolean) -> Unit) -> Unit,
    onLogout: () -> Unit,
    onNavigateToLogin: () -> Unit,
    supabaseUrl: String,
    supabaseKey: String,
    isSupabaseConfigured: Boolean,
    onSaveSupabaseConfig: (String, String) -> Unit,
    onTestSupabaseConnection: ((Boolean, String) -> Unit) -> Unit
) {
    if (currentUser == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Guest User", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Text("Log in to view and manage your profile details, donations, and settings.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary), modifier = Modifier.padding(vertical = 8.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Log In or Register")
                    }
                }
            }
        }
        return
    }

    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(currentUser.full_name) }
    var phone by remember { mutableStateOf(currentUser.phone) }
    var address by remember { mutableStateOf(currentUser.address) }
    var updateSuccessMsg by remember { mutableStateOf<String?>(null) }

    // Supabase config dialog state
    var showSupabaseConfig by remember { mutableStateOf(false) }
    var editUrl by remember { mutableStateOf(supabaseUrl) }
    var editKey by remember { mutableStateOf(supabaseKey) }
    var connectionTestResult by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var isTestingConnection by remember { mutableStateOf(false) }

    if (showSupabaseConfig) {
        AlertDialog(
            onDismissRequest = { showSupabaseConfig = false },
            title = { Text("Supabase Cloud Database", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Connect MealBridge to your live Supabase PostgreSQL backend by entering your project credentials.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    OutlinedTextField(
                        value = editUrl,
                        onValueChange = { editUrl = it },
                        label = { Text("NEXT_PUBLIC_SUPABASE_URL") },
                        placeholder = { Text("https://your-ref.supabase.co") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editKey,
                        onValueChange = { editKey = it },
                        label = { Text("NEXT_PUBLIC_SUPABASE_ANON_KEY") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    connectionTestResult?.let { (success, msg) ->
                        Surface(
                            color = if (success) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (success) GreenDark else Color(0xFF991B1B)
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            isTestingConnection = true
                            connectionTestResult = null
                            onTestSupabaseConnection { success, msg ->
                                isTestingConnection = false
                                connectionTestResult = Pair(success, msg)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isTestingConnection
                    ) {
                        if (isTestingConnection) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp))
                        } else {
                            Text("Test Cloud Connection")
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveSupabaseConfig(editUrl, editKey)
                        showSupabaseConfig = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Save Configuration")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSupabaseConfig = false }) {
                    Text("Close")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Info Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(GreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = GreenDark,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentUser.full_name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = currentUser.email,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = OrangeLight,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = currentUser.user_type,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OrangeHover
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = if (currentUser.status == "Active") GreenLight else Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = currentUser.status,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentUser.status == "Active") GreenDark else Color(0xFFDC2626)
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        if (updateSuccessMsg != null) {
            item {
                Surface(
                    color = GreenLight,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = updateSuccessMsg ?: "",
                        color = GreenDark,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Editable Details Card
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
                        Text("Profile Details", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        TextButton(onClick = { isEditing = !isEditing }) {
                            Text(if (isEditing) "Cancel" else "Edit")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!isEditing) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            DetailItem(label = "Phone Number", value = currentUser.phone)
                            DetailItem(label = "Address", value = currentUser.address)
                            DetailItem(label = "User ID", value = currentUser.id)
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Full Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("Phone Number") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("Address") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    onUpdateProfile(name, phone, address) { success ->
                                        if (success) {
                                            isEditing = false
                                            updateSuccessMsg = "Profile updated permanently!"
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Save Changes")
                            }
                        }
                    }
                }
            }
        }

        // Supabase Cloud Sync Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CloudSync, contentDescription = null, tint = GreenPrimary)
                            Text("Supabase Cloud Sync", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        }
                        Surface(
                            color = if (isSupabaseConfigured) GreenLight else Color(0xFFFEF3C7),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isSupabaseConfigured) "Connected" else "Local DB Active",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSupabaseConfigured) GreenDark else Color(0xFFB45309)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Data is permanently preserved locally in SQLite Room and syncs directly to your Supabase PostgreSQL cloud tables.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { showSupabaseConfig = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Configure Supabase Credentials")
                    }
                }
            }
        }

        // Logout Button
        item {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("logout_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFDC2626))
                )
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
    }
}
