package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.remote.WhatsAppService
import com.example.ui.components.MealBridgeTopBar
import com.example.ui.screens.*
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.MealBridgeTheme
import com.example.ui.viewmodel.MealBridgeViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MealBridgeViewModel by viewModels {
        val app = application as MealBridgeApp
        MealBridgeViewModel.Factory(app.repository, app.supabaseClient)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MealBridgeTheme {
                MealBridgeMainApp(viewModel = viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Available : Screen("available", "Available Food", Icons.Default.Search)
    object Donate : Screen("donate", "Donate Food", Icons.Default.AddCircle)
    object Request : Screen("request", "Food Requests", Icons.Default.SoupKitchen)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object MyDonations : Screen("my_donations", "My Donations")
    object MyRequests : Screen("my_requests", "My Requests")
    object DonationDetails : Screen("donation_details", "Donation Details")
    object Notifications : Screen("notifications", "Notifications")
    object Profile : Screen("profile", "My Profile")
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")
    object Admin : Screen("admin", "Admin Dashboard")
    object About : Screen("about", "About MealBridge")
    object Contact : Screen("contact", "Contact Us")
    object WhatsAppHub : Screen("whatsapp_hub", "WhatsApp Hub")
}

@Composable
fun MealBridgeMainApp(viewModel: MealBridgeViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val liveStats by viewModel.liveStats.collectAsStateWithLifecycle()
    val totalUsers by viewModel.totalUsersCount.collectAsStateWithLifecycle()
    val totalRequests by viewModel.totalRequestsCount.collectAsStateWithLifecycle()
    val availableDonations by viewModel.availableDonations.collectAsStateWithLifecycle()
    val allDonations by viewModel.allDonations.collectAsStateWithLifecycle()
    val allFoodRequests by viewModel.allFoodRequests.collectAsStateWithLifecycle()
    val myDonations by viewModel.myDonations.collectAsStateWithLifecycle()
    val myRequests by viewModel.myFoodRequests.collectAsStateWithLifecycle()
    val notifications by viewModel.myNotifications.collectAsStateWithLifecycle()
    val unreadNotificationsCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val allProfiles by viewModel.allProfiles.collectAsStateWithLifecycle()
    val contactMessages by viewModel.allContactMessages.collectAsStateWithLifecycle()
    val selectedDonation by viewModel.selectedDonation.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Available,
        Screen.Donate,
        Screen.Request,
        Screen.Dashboard
    )

    val currentScreenTitle = when (currentRoute) {
        Screen.Home.route -> "Community Food Sharing"
        Screen.Available.route -> "Available Food Nearby"
        Screen.Donate.route -> "Donate Surplus Food"
        Screen.Request.route -> "Food Requests"
        Screen.Dashboard.route -> "Community Dashboard"
        Screen.MyDonations.route -> "My Food Donations"
        Screen.MyRequests.route -> "My Food Requests"
        Screen.DonationDetails.route -> "Food Details"
        Screen.Notifications.route -> "Notifications & Alerts"
        Screen.Profile.route -> "User Profile & Cloud Sync"
        Screen.Login.route -> "Sign In"
        Screen.Register.route -> "Create Account"
        Screen.Admin.route -> "Admin Control Panel"
        Screen.About.route -> "About MealBridge"
        Screen.Contact.route -> "Contact Support"
        Screen.WhatsAppHub.route -> "WhatsApp Communication Hub"
        else -> "MealBridge"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MealBridgeTopBar(
                title = currentScreenTitle,
                unreadNotifications = unreadNotificationsCount,
                onNotificationsClick = {
                    navController.navigate(Screen.Notifications.route)
                },
                onProfileClick = {
                    if (currentUser == null) {
                        navController.navigate(Screen.Login.route)
                    } else {
                        navController.navigate(Screen.Profile.route)
                    }
                },
                onWhatsAppClick = {
                    navController.navigate(Screen.WhatsAppHub.route)
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("mealbridge_bottom_navigation")
            ) {
                bottomNavItems.forEach { screen ->
                    val isSelected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(Screen.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            screen.icon?.let { icon ->
                                Icon(
                                    imageVector = icon,
                                    contentDescription = screen.title,
                                    tint = if (isSelected) GreenDark else Color(0xFF64748B)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) GreenDark else Color(0xFF64748B)
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = GreenPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.route}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        liveStats = liveStats,
                        totalUsers = totalUsers,
                        totalRequests = totalRequests,
                        onNavigateToDonate = { navController.navigate(Screen.Donate.route) },
                        onNavigateToRequest = { navController.navigate(Screen.Request.route) },
                        onNavigateToAvailable = { navController.navigate(Screen.Available.route) },
                        onNavigateToWhatsApp = { navController.navigate(Screen.WhatsAppHub.route) }
                    )
                }

                composable(Screen.Available.route) {
                    AvailableFoodScreen(
                        donations = availableDonations,
                        onRequestFood = { donation ->
                            if (currentUser == null) {
                                navController.navigate(Screen.Login.route)
                            } else {
                                viewModel.requestSpecificDonation(donation) { success, msg ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(msg)
                                    }
                                }
                            }
                        },
                        onShareOnWhatsApp = { donation ->
                            WhatsAppService.shareDonation(context, donation)
                        },
                        onSelectDonation = { donation ->
                            viewModel.selectDonation(donation)
                            navController.navigate(Screen.DonationDetails.route)
                        }
                    )
                }

                composable(Screen.Donate.route) {
                    DonateFoodScreen(
                        isLoggedIn = currentUser != null,
                        onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                        onSubmitDonation = { foodName, category, qty, people, type, prep, exp, addr, city, phone, desc, img, onSuccess, onError ->
                            viewModel.submitDonation(foodName, category, qty, people, type, prep, exp, addr, city, phone, desc, img, onSuccess, onError)
                        },
                        onShareOnWhatsApp = { donation ->
                            WhatsAppService.shareDonation(context, donation)
                        },
                        onChatOnWhatsApp = { donation ->
                            WhatsAppService.openChat(
                                context = context,
                                message = "Hello MealBridge! I have submitted a food donation. Donation ID: ${donation.donation_id}. Food: ${donation.food_name}."
                            )
                        }
                    )
                }

                composable(Screen.Request.route) {
                    RequestFoodScreen(
                        isLoggedIn = currentUser != null,
                        onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                        onSubmitRequest = { cat, qty, people, date, time, loc, contactNum, reason, notes, onSuccess, onError ->
                            viewModel.submitFoodRequest(cat, qty, people, date, time, loc, contactNum, reason, notes, onSuccess, onError)
                        },
                        onShareOnWhatsApp = { req ->
                            WhatsAppService.shareFoodRequest(context, req)
                        },
                        onChatOnWhatsApp = { req ->
                            WhatsAppService.openChat(
                                context = context,
                                message = "Hello MealBridge! I have submitted a food request. Request ID: ${req.request_id}. Food needed: ${req.category} (${req.quantity}) in ${req.location}."
                            )
                        }
                    )
                }

                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        currentUser = currentUser,
                        liveStats = liveStats,
                        totalUsers = totalUsers,
                        totalRequests = totalRequests,
                        myDonations = myDonations,
                        myRequests = myRequests,
                        recentNotifications = notifications,
                        onNavigateToDonate = { navController.navigate(Screen.Donate.route) },
                        onNavigateToRequest = { navController.navigate(Screen.Request.route) },
                        onNavigateToAvailable = { navController.navigate(Screen.Available.route) },
                        onNavigateToMyDonations = { navController.navigate(Screen.MyDonations.route) },
                        onNavigateToMyRequests = { navController.navigate(Screen.MyRequests.route) },
                        onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                        onNavigateToAdmin = { navController.navigate(Screen.Admin.route) },
                        onNavigateToWhatsAppHelp = { navController.navigate(Screen.WhatsAppHub.route) },
                        onNavigateToLogin = { navController.navigate(Screen.Login.route) }
                    )
                }

                composable(Screen.MyDonations.route) {
                    MyDonationsScreen(
                        donations = myDonations,
                        onSelectDonation = { donation ->
                            viewModel.selectDonation(donation)
                            navController.navigate(Screen.DonationDetails.route)
                        },
                        onShareOnWhatsApp = { donation ->
                            WhatsAppService.shareDonation(context, donation)
                        },
                        onChatOnWhatsApp = { donation ->
                            WhatsAppService.openChat(
                                context = context,
                                message = "Hello MealBridge! Checking update for my donation ${donation.donation_id} (${donation.food_name})."
                            )
                        },
                        onNavigateToDonate = { navController.navigate(Screen.Donate.route) }
                    )
                }

                composable(Screen.MyRequests.route) {
                    MyRequestsScreen(
                        requests = myRequests,
                        onShareOnWhatsApp = { req ->
                            WhatsAppService.shareFoodRequest(context, req)
                        },
                        onChatOnWhatsApp = { req ->
                            WhatsAppService.openChat(
                                context = context,
                                message = "Hello MealBridge! Following up on food request ${req.request_id} for ${req.category}."
                            )
                        },
                        onNavigateToRequest = { navController.navigate(Screen.Request.route) }
                    )
                }

                composable(Screen.DonationDetails.route) {
                    DonationDetailsScreen(
                        donation = selectedDonation,
                        onBackClick = { navController.popBackStack() },
                        onRequestFood = { donation ->
                            if (currentUser == null) {
                                navController.navigate(Screen.Login.route)
                            } else {
                                viewModel.requestSpecificDonation(donation) { success, msg ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(msg)
                                    }
                                }
                            }
                        },
                        onShareOnWhatsApp = { donation ->
                            WhatsAppService.shareDonation(context, donation)
                        },
                        onChatOnWhatsApp = { donation ->
                            WhatsAppService.openChat(
                                context = context,
                                message = "Hello MealBridge! I am interested in donation ${donation.donation_id} (${donation.food_name})."
                            )
                        }
                    )
                }

                composable(Screen.Notifications.route) {
                    NotificationsScreen(
                        notifications = notifications,
                        onMarkAsRead = { id -> viewModel.markNotificationRead(id) },
                        onMarkAllAsRead = { viewModel.markAllNotificationsRead() }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen(
                        currentUser = currentUser,
                        onUpdateProfile = { name, phone, address, onResult ->
                            viewModel.updateProfile(name, phone, address, onResult)
                        },
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0)
                            }
                        },
                        onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                        supabaseUrl = viewModel.getSupabaseUrl(),
                        supabaseKey = viewModel.getSupabaseAnonKey(),
                        isSupabaseConfigured = viewModel.isSupabaseConfigured(),
                        onSaveSupabaseConfig = { url, key -> viewModel.saveSupabaseConfig(url, key) },
                        onTestSupabaseConnection = { callback -> viewModel.testSupabaseConnection(callback) }
                    )
                }

                composable(Screen.Login.route) {
                    LoginScreen(
                        onLogin = { email, onResult -> viewModel.login(email, onResult) },
                        onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                        onLoginSuccess = {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Home.route)
                            }
                        }
                    )
                }

                composable(Screen.Register.route) {
                    RegisterScreen(
                        onRegister = { name, email, phone, userType, address, onResult ->
                            viewModel.register(name, email, phone, userType, address, onResult)
                        },
                        onNavigateToLogin = { navController.navigate(Screen.Login.route) }
                    )
                }

                composable(Screen.Admin.route) {
                    AdminDashboardScreen(
                        liveStats = liveStats,
                        users = allProfiles,
                        donations = allDonations,
                        requests = allFoodRequests,
                        contactMessages = contactMessages,
                        onToggleUserStatus = { id, current -> viewModel.toggleUserStatus(id, current) },
                        onUpdateDonationStatus = { id, status -> viewModel.updateDonationStatus(id, status) },
                        onDeleteDonation = { id -> viewModel.deleteDonation(id) },
                        onUpdateRequestStatus = { id, status -> viewModel.updateRequestStatus(id, status) }
                    )
                }

                composable(Screen.About.route) {
                    AboutScreen(
                        onChatOnWhatsApp = { msg ->
                            WhatsAppService.openChat(context, message = msg)
                        }
                    )
                }

                composable(Screen.Contact.route) {
                    ContactScreen(
                        onSubmitMessage = { name, email, phone, subject, msg, onResult ->
                            viewModel.submitContact(name, email, phone, subject, msg, onResult)
                        },
                        onChatOnWhatsApp = { msg ->
                            WhatsAppService.openChat(context, message = msg)
                        }
                    )
                }

                composable(Screen.WhatsAppHub.route) {
                    WhatsAppSectionScreen(
                        onSendWhatsApp = { msg ->
                            WhatsAppService.openChat(context, message = msg)
                        }
                    )
                }
            }
        }
    }
}
