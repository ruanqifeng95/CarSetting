package com.example.carsetting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carsetting.ui.components.ToggleSettingCard

@Composable
fun SafetySettingsTab() {
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
            var bsd by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = "盲点监测",
                icon = Icons.Default.Visibility,
                description = "监测盲区车辆",
                checked = bsd,
                onCheckedChange = { bsd = it }
            )
        }
        
        item {
            var lka by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = "车道保持辅助",
                icon = Icons.Default.Straighten,
                description = "帮助保持在车道内",
                checked = lka,
                onCheckedChange = { lka = it }
            )
        }
        
        item {
            var fcw by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = "碰撞预警",
                icon = Icons.Default.Warning,
                description = "前方碰撞警告",
                checked = fcw,
                onCheckedChange = { fcw = it }
            )
        }
        
        item {
            var aeb by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = "自动紧急制动",
                icon = Icons.Default.CarCrash,
                description = "检测到危险时自动刹车",
                checked = aeb,
                onCheckedChange = { aeb = it }
            )
        }
    }
}
