package com.example.carsetting.repository

import com.example.carsetting.model.DrivingSettingsState

interface DrivingSettingsRepository {

    suspend fun loadSettings(): DrivingSettingsState

    suspend fun saveSettings(settings: DrivingSettingsState)
}