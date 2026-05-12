package com.example.carsetting.model
enum class DrivingMode(val displayName: String) {
    Eco("经济"),
    Standard("标准"),
    Sport("运动"),
    OffRoad("越野");

    companion object {
        fun fromDisplayName(value: String): DrivingMode {
            return entries.find { it.displayName == value }
                ?: Standard
        }
    }
}
data class DrivingSettingsState(
    val drivingMode: DrivingMode = DrivingMode.Standard,
    val autoStartStop: Boolean = false,
    val energyRecovery: Boolean = true,
    val steeringEffort: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
)