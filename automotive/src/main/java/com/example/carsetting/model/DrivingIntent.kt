package com.example.carsetting.model

sealed class DrivingIntent {
    data class ChangeDrivingMode(val mode: DrivingMode) : DrivingIntent()
    data class ToggleAutoStartStop(val enabled: Boolean) : DrivingIntent()
    data class ToggleEnergyRecovery(val enabled: Boolean) : DrivingIntent()
    data class ChangeSteeringEffort(val level: Int) : DrivingIntent()
    object LoadSettings : DrivingIntent()
    object DismissError : DrivingIntent()
}
