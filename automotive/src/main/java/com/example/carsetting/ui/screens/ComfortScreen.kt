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
import androidx.compose.ui.res.stringResource
import com.example.carsetting.settings.R
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
                title = stringResource(R.string.ac_auto),
                icon = Icons.Default.AcUnit,
                description = stringResource(R.string.ac_auto_desc),
                checked = autoAc,
                onCheckedChange = { autoAc = it }
            )
        }
        
        item {
            var seatTemp by remember { mutableFloatStateOf(0f) }
            val labels = listOf(
                stringResource(R.string.seat_off),
                stringResource(R.string.seat_low),
                stringResource(R.string.seat_mid),
                stringResource(R.string.seat_high)
            )
            SliderSettingCard(
                title = stringResource(R.string.seat_temp),
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
                title = stringResource(R.string.ambient_light),
                icon = Icons.Default.Lightbulb,
                description = stringResource(R.string.ambient_light_desc),
                checked = ambientLight,
                onCheckedChange = { ambientLight = it }
            )
        }
        
        item {
            var volumeLevel by remember { mutableStateOf("Mid") }
            val options = listOf(
                stringResource(R.string.seat_low),
                stringResource(R.string.seat_mid),
                stringResource(R.string.seat_high)
            )
            SettingCard(
                title = stringResource(R.string.volume_settings),
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                options = options,
                selectedOption = when(volumeLevel) {
                    "Low" -> options[0]
                    "Mid" -> options[1]
                    else -> options[2]
                },
                onOptionSelected = { 
                    volumeLevel = when(it) {
                        options[0] -> "Low"
                        options[1] -> "Mid"
                        else -> "High"
                    }
                }
            )
        }
    }
}
