package com.example.carsetting.repository

import com.example.carsetting.model.ConnectivityState

interface ConnectivityRepository {
    suspend fun loadSettings(): ConnectivityState
    suspend fun saveWifiSetting(enabled: Boolean)
}
