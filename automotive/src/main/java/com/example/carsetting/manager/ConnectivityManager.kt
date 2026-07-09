package com.example.carsetting.manager

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityManager {
    val musicInfo: StateFlow<String>
    fun openBluetoothSettings()
    fun openMusicPlayer()
    fun release()
}
