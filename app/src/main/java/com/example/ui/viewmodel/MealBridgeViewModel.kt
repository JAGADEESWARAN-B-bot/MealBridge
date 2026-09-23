package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppNotification
import com.example.data.model.ContactMessage
import com.example.data.model.FoodDonation
import com.example.data.model.FoodRequest
import com.example.data.model.UserProfile
import com.example.data.remote.SupabaseClient
import com.example.data.repository.LiveStats
import com.example.data.repository.MealBridgeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MealBridgeViewModel(
    private val repository: MealBridgeRepository,
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    val currentUser: StateFlow<UserProfile?> = repository.currentUser

    val liveStats: StateFlow<LiveStats> = repository.stats.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LiveStats()
    )

    val totalUsersCount: StateFlow<Int> = repository.totalUsersCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalRequestsCount: StateFlow<Int> = repository.totalRequestsCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val availableDonations: StateFlow<List<FoodDonation>> = repository.availableDonations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allDonations: StateFlow<List<FoodDonation>> = repository.allDonations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allFoodRequests: StateFlow<List<FoodRequest>> = repository.allFoodRequests.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val myDonations: StateFlow<List<FoodDonation>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getMyDonations(user.id) else flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val myFoodRequests: StateFlow<List<FoodRequest>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getMyFoodRequests(user.id) else flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val myNotifications: StateFlow<List<AppNotification>> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getMyNotifications(user.id) else flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val unreadNotificationsCount: StateFlow<Int> = currentUser.flatMapLatest { user ->
        if (user != null) repository.getUnreadCount(user.id) else flowOf(0)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val allProfiles: StateFlow<List<UserProfile>> = repository.allProfiles.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allContactMessages: StateFlow<List<ContactMessage>> = repository.allContactMessages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedDonation = MutableStateFlow<FoodDonation?>(null)
    val selectedDonation: StateFlow<FoodDonation?> = _selectedDonation.asStateFlow()

    fun selectDonation(donation: FoodDonation?) {
        _selectedDonation.value = donation
    }

    // Feedback message state
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    // Auth actions
    fun login(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val res = repository.loginUser(email)
            if (res.isSuccess) {
                onResult(true, "Welcome back, ${res.getOrNull()?.full_name}!")
            } else {
                onResult(false, res.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun register(
        fullName: String,
        email: String,
        phone: String,
        userType: String,
        address: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.registerUser(fullName, email, phone, userType, address)
            if (res.isSuccess) {
                onResult(true, "Registration successful! You can now log in.")
            } else {
                onResult(false, res.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        repository.logout()
    }

    fun updateProfile(name: String, phone: String, address: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = repository.updateProfile(name, phone, address)
            onResult(res.isSuccess)
        }
    }

    // Donation actions
    fun submitDonation(
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
    ) {
        viewModelScope.launch {
            val res = repository.createDonation(
                foodName = foodName,
                category = category,
                quantity = quantity,
                peopleServed = peopleServed,
                foodType = foodType,
                preparationDate = prepDate,
                expiryDate = expiryDate,
                pickupAddress = pickupAddress,
                city = city,
                contactNumber = contact,
                description = description,
                imageUrl = imageUrl
            )
            if (res.isSuccess && res.getOrNull() != null) {
                onSuccess(res.getOrNull()!!)
            } else {
                onError(res.exceptionOrNull()?.message ?: "Failed to submit donation")
            }
        }
    }

    fun submitFoodRequest(
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
    ) {
        viewModelScope.launch {
            val res = repository.createFoodRequest(
                category = category,
                quantity = quantity,
                peopleCount = peopleCount,
                requiredDate = requiredDate,
                requiredTime = requiredTime,
                location = location,
                contactNumber = contact,
                reason = reason,
                notes = notes
            )
            if (res.isSuccess && res.getOrNull() != null) {
                onSuccess(res.getOrNull()!!)
            } else {
                onError(res.exceptionOrNull()?.message ?: "Failed to submit request")
            }
        }
    }

    fun requestSpecificDonation(donation: FoodDonation, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val res = repository.requestSpecificDonation(donation)
            if (res.isSuccess) {
                onResult(true, "Request sent to donor! They will be notified.")
            } else {
                onResult(false, res.exceptionOrNull()?.message ?: "Failed to send request")
            }
        }
    }

    fun updateDonationStatus(donationId: String, status: String) {
        viewModelScope.launch {
            repository.updateDonationStatus(donationId, status)
        }
    }

    fun deleteDonation(donationId: String) {
        viewModelScope.launch {
            repository.deleteDonation(donationId)
        }
    }

    fun updateRequestStatus(requestId: String, status: String) {
        viewModelScope.launch {
            repository.updateFoodRequestStatus(requestId, status)
        }
    }

    fun toggleUserStatus(userId: String, currentStatus: String) {
        viewModelScope.launch {
            val newStatus = if (currentStatus == "Active") "Inactive" else "Active"
            repository.toggleUserStatus(userId, newStatus)
        }
    }

    fun markNotificationRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsRead() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.markAllNotificationsAsRead(user.id)
        }
    }

    fun submitContact(name: String, email: String, phone: String, subject: String, message: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = repository.submitContactMessage(name, email, phone, subject, message)
            onResult(res.isSuccess)
        }
    }

    // Supabase config
    fun getSupabaseUrl(): String = supabaseClient.supabaseUrl
    fun getSupabaseAnonKey(): String = supabaseClient.supabaseAnonKey
    fun isSupabaseConfigured(): Boolean = supabaseClient.isConfigured

    fun saveSupabaseConfig(url: String, key: String) {
        supabaseClient.supabaseUrl = url
        supabaseClient.supabaseAnonKey = key
    }

    fun testSupabaseConnection(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val (success, msg) = supabaseClient.testConnection()
            onResult(success, msg)
        }
    }

    class Factory(
        private val repository: MealBridgeRepository,
        private val supabaseClient: SupabaseClient
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MealBridgeViewModel(repository, supabaseClient) as T
        }
    }
}
