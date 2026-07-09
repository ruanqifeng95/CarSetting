package com.example.carsetting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.carsetting.manager.ConnectivityManager
import com.example.carsetting.manager.ConnectivityManagerImpl
import com.example.carsetting.model.ConnectivityIntent
import com.example.carsetting.model.ConnectivityState
import com.example.carsetting.repository.ConnectivityRepository
import com.example.carsetting.repository.ConnectivityRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConnectivityViewModel(
    private val repository: ConnectivityRepository = ConnectivityRepositoryImpl(),
    private val manager: ConnectivityManager? = null
) : ViewModel() {

    private val _state = MutableStateFlow(ConnectivityState())
    val state: StateFlow<ConnectivityState> = _state.asStateFlow()

    init {
        handleIntent(ConnectivityIntent.LoadSettings)
        observeMusicInfo()
    }

    private fun observeMusicInfo() {
        manager?.let {
            viewModelScope.launch {
                it.musicInfo.collect { info ->
                    updateMusicInfo(info)
                }
            }
        }
    }

    fun handleIntent(intent: ConnectivityIntent) {
        when (intent) {
            is ConnectivityIntent.ToggleWifi -> toggleWifi(intent.enabled)
            is ConnectivityIntent.UpdateMusicInfo -> updateMusicInfo(intent.info)
            ConnectivityIntent.LoadSettings -> loadSettings()
            ConnectivityIntent.OpenBluetoothSettings -> manager?.openBluetoothSettings()
            ConnectivityIntent.OpenMusicPlayer -> manager?.openMusicPlayer()
        }
    }

    private fun toggleWifi(enabled: Boolean) {
        viewModelScope.launch {
            _state.update { it.copy(wifiEnabled = enabled) }
            repository.saveWifiSetting(enabled)
        }
    }

    private fun updateMusicInfo(info: String) {
        _state.update { it.copy(currentMusicInfo = info) }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val loadedState = repository.loadSettings()
                _state.update { 
                    it.copy(
                        wifiEnabled = loadedState.wifiEnabled,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    override fun onCleared() {
        manager?.release()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return ConnectivityViewModel(
                    repository = ConnectivityRepositoryImpl(), // 以后可以替换为真实的 Repository
                    manager = ConnectivityManagerImpl(application.applicationContext)
                ) as T
            }
        }
    }
}
