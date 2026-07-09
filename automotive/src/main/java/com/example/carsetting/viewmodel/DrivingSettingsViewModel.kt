package com.example.carsetting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carsetting.model.DrivingIntent
import com.example.carsetting.model.DrivingMode
import com.example.carsetting.model.DrivingSettingsState
import com.example.carsetting.repository.DrivingSettingsRepository
import com.example.carsetting.repository.DrivingSettingsRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DrivingSettingsViewModel(
    private val repository: DrivingSettingsRepository = DrivingSettingsRepositoryImpl()
) : ViewModel() {
    
    private val _state = MutableStateFlow(DrivingSettingsState())
    val state: StateFlow<DrivingSettingsState> = _state.asStateFlow()
    
    init {
        handleIntent(DrivingIntent.LoadSettings)
    }
    
    fun handleIntent(intent: DrivingIntent) {
        when (intent) {
            is DrivingIntent.ChangeDrivingMode -> changeDrivingMode(intent.mode)
            is DrivingIntent.ToggleAutoStartStop -> toggleAutoStartStop(intent.enabled)
            is DrivingIntent.ToggleEnergyRecovery -> toggleEnergyRecovery(intent.enabled)
            is DrivingIntent.ChangeSteeringEffort -> changeSteeringEffort(intent.level)
            DrivingIntent.LoadSettings -> loadSettings()
            DrivingIntent.DismissError -> dismissError()
        }
    }
    
    private fun changeDrivingMode(mode: DrivingMode) {
        _state.update { currentState ->
            currentState.copy(
                drivingMode = mode,
                isLoading = false
            )
        }
    }
    
    private fun toggleAutoStartStop(enabled: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                autoStartStop = enabled,
                isLoading = false
            )
        }
    }
    
    private fun toggleEnergyRecovery(enabled: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                energyRecovery = enabled,
                isLoading = false
            )
        }
    }
    
    private fun changeSteeringEffort(level: Int) {
        _state.update { currentState ->
            currentState.copy(
                steeringEffort = level,
                isLoading = false
            )
        }
    }

    private fun loadSettings() {

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            try {

                val settings = repository.loadSettings()

                _state.update {
                    settings.copy(isLoading = false)
                }

            } catch (e: Exception) {

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
    
    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}
