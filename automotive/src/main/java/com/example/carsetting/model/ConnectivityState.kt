package com.example.carsetting.model

data class ConnectivityState(
    val wifiEnabled: Boolean = false,
    val currentMusicInfo: String = "打开内置音乐播放器",
    val isLoading: Boolean = false,
    val error: String? = null
)
