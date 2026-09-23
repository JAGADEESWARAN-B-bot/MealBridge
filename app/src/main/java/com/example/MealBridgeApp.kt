package com.example

import android.app.Application
import com.example.data.local.MealBridgeDatabase
import com.example.data.remote.SupabaseClient
import com.example.data.repository.MealBridgeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MealBridgeApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    val database by lazy { MealBridgeDatabase.getDatabase(this, applicationScope) }
    val supabaseClient by lazy { SupabaseClient(this) }
    val repository by lazy {
        MealBridgeRepository(
            context = this,
            dao = database.dao(),
            supabaseClient = supabaseClient,
            scope = applicationScope
        )
    }

    override fun onCreate() {
        super.onCreate()
    }
}
