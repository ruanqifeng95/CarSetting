package com.example.carsetting.repository

import com.example.carsetting.model.DrivingMode
import com.example.carsetting.model.DrivingSettingsState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class DrivingSettingsRepositoryImpl : DrivingSettingsRepository {

    override suspend fun loadSettings(): DrivingSettingsState {

        delay(500.milliseconds)

        return DrivingSettingsState(
            drivingMode = DrivingMode.Standard,
            autoStartStop = false,
            energyRecovery = true,
            steeringEffort = 1
        )
    }

    override suspend fun saveSettings(settings: DrivingSettingsState) {

        delay(500.milliseconds)

        println("Settings saved: $settings")
    }
}