package com.example.carsetting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.carsetting.ui.components.NavigationEntryCard
import com.example.carsetting.ui.components.ToggleSettingCard

@Composable
fun ConnectivitySettingsTab() {
    val context = LocalContext.current

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
