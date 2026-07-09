package com.example.carsetting.repository

import com.example.carsetting.model.ConnectivityState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class ConnectivityRepositoryImpl : ConnectivityRepository {

    override suspend fun loadSettings(): ConnectivityState {
        delay(800.milliseconds)
        return ConnectivityState(
            wifiEnabled = false,
            currentMusicInfo = "打开内置音乐播放器"
        )
    }

    override suspend fun saveWifiSetting(enabled: Boolean) {
        delay(300.milliseconds)
        println("Wifi setting saved: $enabled")
    }
}
