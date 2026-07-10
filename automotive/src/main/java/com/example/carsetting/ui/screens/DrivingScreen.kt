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
import androidx.compose.ui.res.stringResource
import com.example.carsetting.settings.R

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
            val options = listOf(
                stringResource(R.string.driving_mode_eco),
                stringResource(R.string.driving_mode_standard),
                stringResource(R.string.driving_mode_sport),
                stringResource(R.string.driving_mode_offroad)
            )
            val selectedOption = when (state.drivingMode) {
                DrivingMode.Eco -> options[0]
                DrivingMode.Standard -> options[1]
                DrivingMode.Sport -> options[2]
                DrivingMode.OffRoad -> options[3]
            }

            SettingCard(
                title = stringResource(R.string.driving_mode),
                icon = Icons.Default.Speed,
                options = options,
                selectedOption = selectedOption,
                onOptionSelected = { displayName ->
                    val mode = when (displayName) {
                        options[0] -> DrivingMode.Eco
                        options[1] -> DrivingMode.Standard
                        options[2] -> DrivingMode.Sport
                        options[3] -> DrivingMode.OffRoad
                        else -> DrivingMode.Standard
                    }
                    viewModel.handleIntent(DrivingIntent.ChangeDrivingMode(mode))
                }
            )
        }

        item {
            NavigationEntryCard(
                title = stringResource(R.string.battery_preservation),
                description = stringResource(R.string.battery_preservation_desc),
                icon = Icons.Default.BatteryChargingFull,
                onClick = onNavigateToBatteryPreservation
            )
        }
        
        item {
            ToggleSettingCard(
                title = stringResource(R.string.auto_start_stop),
                description = stringResource(R.string.auto_start_stop_desc),
                icon = Icons.Default.PlayCircle,
                checked = state.autoStartStop,
                onCheckedChange = { viewModel.handleIntent(DrivingIntent.ToggleAutoStartStop(it)) }
            )
        }
        
        item {
            ToggleSettingCard(
                title = stringResource(R.string.energy_recovery),
                description = stringResource(R.string.energy_recovery_desc),
                icon = Icons.Default.BatteryChargingFull,
                checked = state.energyRecovery,
                onCheckedChange = { viewModel.handleIntent(DrivingIntent.ToggleEnergyRecovery(it)) }
            )
        }
        
        item {
            val steeringLabels = listOf(
                stringResource(R.string.steering_light),
                stringResource(R.string.steering_standard),
                stringResource(R.string.steering_sport)
            )
            SliderSettingCard(
                title = stringResource(R.string.steering_effort),
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
    var preservationMode by remember { mutableStateOf("Smart") } // Internal state, will be mapped to resources
    var targetSoc by remember { mutableFloatStateOf(50f) }
    
    val bpSmart = stringResource(R.string.bp_smart)
    val bpForced = stringResource(R.string.bp_forced)
    
    val isSocEnabled = preservationMode == "Forced"

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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Text(
                text = stringResource(R.string.battery_preservation),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        SettingCard(
            title = stringResource(R.string.bp_mode),
            options = listOf(bpSmart, bpForced),
            selectedOption = if (preservationMode == "Smart") bpSmart else bpForced,
            onOptionSelected = { if (it == bpSmart) preservationMode = "Smart" else preservationMode = "Forced" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SliderSettingCard(
            title = stringResource(R.string.bp_target_soc),
            value = targetSoc,
            onValueChange = { targetSoc = it },
            valueRange = 20f..80f,
            steps = 5,
            label = "${targetSoc.toInt()}%",
            enabled = isSocEnabled,
            description = if (isSocEnabled) stringResource(R.string.bp_desc_forced) else stringResource(R.string.bp_desc_smart)
        )
    }
}
