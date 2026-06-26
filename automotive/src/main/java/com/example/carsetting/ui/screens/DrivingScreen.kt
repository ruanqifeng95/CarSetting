package com.example.carsetting.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carsetting.model.DrivingIntent
import com.example.carsetting.model.DrivingMode
import com.example.carsetting.ui.components.*
import com.example.carsetting.viewmodel.DrivingSettingsViewModel
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DrivingSettingsTab(
    viewModel: DrivingSettingsViewModel = viewModel(),
    onNavigateToBatteryPreservation: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
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
            SettingCard(
                title = "驾驶模式",
                icon = Icons.Default.Speed,
                options = listOf("经济", "标准", "运动", "越野"),
                selectedOption = state.drivingMode.displayName,
                onOptionSelected = { displayName ->
                    val mode = when (displayName) {
                        "经济" -> DrivingMode.Eco
                        "标准" -> DrivingMode.Standard
                        "运动" -> DrivingMode.Sport
                        "越野" -> DrivingMode.OffRoad
                        else -> DrivingMode.Standard
                    }
                    viewModel.handleIntent(DrivingIntent.ChangeDrivingMode(mode))
                }
            )
        }

        item {
            NavigationEntryCard(
                title = "行车保电",
                description = "设置混合动力系统的保电策略",
                icon = Icons.Default.BatteryChargingFull,
                onClick = onNavigateToBatteryPreservation
            )
        }
        
        item {
            ToggleSettingCard(
                title = "自动启停",
                description = "在停车时自动关闭发动机",
                icon = Icons.Default.PlayCircle,
                checked = state.autoStartStop,
                onCheckedChange = { viewModel.handleIntent(DrivingIntent.ToggleAutoStartStop(it)) }
            )
        }
        
        item {
            ToggleSettingCard(
                title = "能量回收",
                description = "减速时回收能量",
                icon = Icons.Default.BatteryChargingFull,
                checked = state.energyRecovery,
                onCheckedChange = { viewModel.handleIntent(DrivingIntent.ToggleEnergyRecovery(it)) }
            )
        }
        
        item {
            val steeringLabels = listOf("轻便", "标准", "运动")
            SliderSettingCard(
                title = "转向力度",
                icon = Icons.AutoMirrored.Filled.RotateRight,
                value = state.steeringEffort.toFloat(),
                onValueChange = { viewModel.handleIntent(DrivingIntent.ChangeSteeringEffort(it.toInt())) },
                valueRange = 1f..3f,
                steps = 1,
                label = steeringLabels.getOrNull(state.steeringEffort - 1) ?: ""
            )
        }
    }
}

@Composable
fun BatteryPreservationScreen(onBack: () -> Unit) {
    var preservationMode by remember { mutableStateOf("智能保电") }
    var targetSoc by remember { mutableFloatStateOf(50f) }
    val isSocEnabled = preservationMode == "强制保电"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
            }
            Text(
                text = "行车保电",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        SettingCard(
            title = "保电模式",
            options = listOf("智能保电", "强制保电"),
            selectedOption = preservationMode,
            onOptionSelected = { preservationMode = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SliderSettingCard(
            title = "目标电量",
            value = targetSoc,
            onValueChange = { targetSoc = it },
            valueRange = 20f..80f,
            steps = 5,
            label = "${targetSoc.toInt()}%",
            enabled = isSocEnabled,
            description = if (isSocEnabled) "强制保电下，系统将尽量维持电量在设定值附近" else "智能保电下，系统将自动管理电量，无需手动设置"
        )
    }
}
