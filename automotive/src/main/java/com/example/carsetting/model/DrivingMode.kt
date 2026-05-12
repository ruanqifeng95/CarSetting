package com.example.carsetting.model

sealed class DrivingMode {
    object Eco : DrivingMode()
    object Standard : DrivingMode()
    object Sport : DrivingMode()
    object OffRoad : DrivingMode()
    
    fun toDisplayString(): String {
        return when (this) {
            is Eco -> "经济"
            is Standard -> "标准"
            is Sport -> "运动"
            is OffRoad -> "越野"
        }
    }
    
    companion object {
        fun fromString(value: String): DrivingMode {
            return when (value) {
                "经济" -> Eco
                "标准" -> Standard
                "运动" -> Sport
                "越野" -> OffRoad
                else -> Standard
            }
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
