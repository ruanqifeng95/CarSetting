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
import androidx.compose.ui.res.stringResource
import com.example.carsetting.settings.R
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
                title = stringResource(R.string.bsd_title),
                icon = Icons.Default.Visibility,
                description = stringResource(R.string.bsd_desc),
                checked = bsd,
                onCheckedChange = { bsd = it }
            )
        }
        
        item {
            var lka by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = stringResource(R.string.lka_title),
                icon = Icons.Default.Straighten,
                description = stringResource(R.string.lka_desc),
                checked = lka,
                onCheckedChange = { lka = it }
            )
        }
        
        item {
            var fcw by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = stringResource(R.string.fcw_title),
                icon = Icons.Default.Warning,
                description = stringResource(R.string.fcw_desc),
                checked = fcw,
                onCheckedChange = { fcw = it }
            )
        }
        
        item {
            var aeb by remember { mutableStateOf(true) }
            ToggleSettingCard(
                title = stringResource(R.string.aeb_title),
                icon = Icons.Default.CarCrash,
                description = stringResource(R.string.aeb_desc),
                checked = aeb,
                onCheckedChange = { aeb = it }
            )
        }
    }
}
