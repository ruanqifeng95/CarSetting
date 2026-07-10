package com.example.carsetting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carsetting.ui.components.SettingCard
import com.example.carsetting.ui.components.NavigationEntryCard

@Composable
fun GeneralSettingsTab() {
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
            var language by remember { mutableStateOf("简体中文") }
            SettingCard(
                title = "语言",
                icon = Icons.Default.Language,
                options = listOf("简体中文", "English"),
                selectedOption = language,
                onOptionSelected = { language = it }
            )
        }

        item {
            var unit by remember { mutableStateOf("公制") }
            SettingCard(
                title = "单位",
                icon = Icons.Default.Straighten,
                options = listOf("公制", "英制"),
                selectedOption = unit,
                onOptionSelected = { unit = it }
            )
        }

        item {
            NavigationEntryCard(
                title = "系统信息",
                description = "版本号: 1.0.0 (Build 20250707)",
                icon = Icons.Default.Info,
                onClick = { /* Handle about click */ }
            )
        }
        
        item {
            NavigationEntryCard(
                title = "恢复出厂设置",
                description = "清除所有用户数据并重置设置",
                icon = Icons.Default.Settings,
                onClick = { /* Handle reset click */ }
            )
        }
    }
}
