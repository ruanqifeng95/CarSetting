package com.example.carsetting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carsetting.ui.components.SettingCard
import com.example.carsetting.ui.components.SliderSettingCard
import com.example.carsetting.ui.components.ToggleSettingCard

@Composable
fun ComfortSettingsTab() {
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
            var autoAc by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = "空调自动模式",
                icon = Icons.Default.AcUnit,
                description = "自动调节温度和风量",
                checked = autoAc,
                onCheckedChange = { autoAc = it }
            )
        }
        
        item {
            var seatTemp by remember { mutableFloatStateOf(0f) }
            val labels = listOf("关闭", "低", "中", "高")
            SliderSettingCard(
                title = "座椅温度",
                icon = Icons.Default.Whatshot,
                value = seatTemp,
                onValueChange = { seatTemp = it },
                valueRange = 0f..3f,
                steps = 2,
                label = labels.getOrNull(seatTemp.toInt()) ?: ""
            )
        }
        
        item {
            var ambientLight by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = "氛围灯",
                icon = Icons.Default.Lightbulb,
                description = "车内氛围照明",
                checked = ambientLight,
                onCheckedChange = { ambientLight = it }
            )
        }
        
        item {
            var volume by remember { mutableStateOf("中") }
            SettingCard(
                title = "音量设置",
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                options = listOf("低", "中", "高"),
                selectedOption = volume,
                onOptionSelected = { volume = it }
            )
        }
    }
}
