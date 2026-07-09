package com.example.carsetting.model

sealed class ConnectivityIntent {
    data class ToggleWifi(val enabled: Boolean) : ConnectivityIntent()
    data class UpdateMusicInfo(val info: String) : ConnectivityIntent()
    object LoadSettings : ConnectivityIntent()
    object OpenBluetoothSettings : ConnectivityIntent()
    object OpenMusicPlayer : ConnectivityIntent()
    object OpenProjectionSettings : ConnectivityIntent()
}
