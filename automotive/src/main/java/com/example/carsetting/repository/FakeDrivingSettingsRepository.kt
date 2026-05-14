package com.example.carsetting.repository

import com.example.carsetting.model.DrivingMode
import com.example.carsetting.model.DrivingSettingsState
import kotlinx.coroutines.delay

class FakeDrivingSettingsRepository : DrivingSettingsRepository {

    override suspend fun loadSettings(): DrivingSettingsState {

        delay(1000)

        return DrivingSettingsState(
            drivingMode = DrivingMode.Standard,
            autoStartStop = false,
            energyRecovery = true,
            steeringEffort = 1
        )
    }

    override suspend fun saveSettings(settings: DrivingSettingsState) {

        delay(500)

        println("Settings saved: $settings")
    }
}