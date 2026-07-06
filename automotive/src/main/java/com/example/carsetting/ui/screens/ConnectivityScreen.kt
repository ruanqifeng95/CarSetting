package com.example.carsetting.ui.screens

import android.content.ComponentName
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.carsetting.ui.components.NavigationEntryCard
import com.example.carsetting.ui.components.ToggleSettingCard
import com.google.common.util.concurrent.MoreExecutors

@Composable
fun ConnectivitySettingsTab() {
    val context = LocalContext.current
    var controller by remember { mutableStateOf<MediaController?>(null) }
    var currentMusicInfo by remember { mutableStateOf("打开内置音乐播放器") }

    DisposableEffect(Unit) {
        val componentName = ComponentName("com.example.carsetting.musicplayer", "com.example.carsetting.shared.MyMusicService")
        val controllerFuture = try {
            val sessionToken = SessionToken(context, componentName)
            MediaController.Builder(context, sessionToken).buildAsync()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        controllerFuture?.addListener({
            try {
                val mediaController = controllerFuture.get()
                controller = mediaController
                
                // 设置初始显示
                val metadata = mediaController.mediaMetadata
                if (metadata.title != null) {
                    currentMusicInfo = "${metadata.title} - ${metadata.artist ?: "未知艺术家"}"
                }

                mediaController.addListener(object : Player.Listener {
                    override fun onMediaMetadataChanged(metadata: MediaMetadata) {
                        if (metadata.title != null) {
                            currentMusicInfo = "${metadata.title} - ${metadata.artist ?: "未知艺术家"}"
                        } else {
                            currentMusicInfo = "未在播放"
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())

        onDispose {
            controllerFuture?.let { MediaController.releaseFuture(it) }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            NavigationEntryCard(
                title = "蓝牙音频",
                description = "连接手机蓝牙播放音乐",
                icon = Icons.Default.Bluetooth,
                iconTint = Color(0xFF2196F3),
                onClick = {
                    val packageName = "com.android.car.media"
                    val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
                    if (launchIntent != null) {
                        context.startActivity(launchIntent)
                    } else {
                        val intent = android.content.Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS)
                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                }
            )
        }
        
        item {
            var wifiEnabled by remember { mutableStateOf(false) }
            ToggleSettingCard(
                title = "车载 Wi-Fi",
                icon = Icons.Default.Wifi,
                description = "管理车内热点连接",
                checked = wifiEnabled,
                onCheckedChange = { wifiEnabled = it }
            )
        }

        item {
            NavigationEntryCard(
                title = "车载音乐",
                description = currentMusicInfo,
                icon = Icons.Default.MusicNote,
                iconTint = Color(0xFF1DB954),
                onClick = {
                    // Android 14+ 要求显式允许 PendingIntent 后台启动 Activity
                    val options = android.app.ActivityOptions.makeBasic()
                    if (Build.VERSION.SDK_INT >= 36) {
                        options.pendingIntentBackgroundActivityStartMode = android.app.ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOW_ALWAYS
                    }
                    else {
                        @Suppress("DEPRECATION")
                        options.pendingIntentBackgroundActivityStartMode = android.app.ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                    }

                    try {
                        controller?.sessionActivity?.send(context, 0, null, null, null, null, options.toBundle())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
        }
        
        item {
            NavigationEntryCard(
                title = "手机投屏",
                description = "无线连接手机投屏",
                icon = Icons.Default.Cast,
                iconTint = MaterialTheme.colorScheme.primary,
                onClick = {
                    val intent = android.content.Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS)
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            )
        }
    }
}

