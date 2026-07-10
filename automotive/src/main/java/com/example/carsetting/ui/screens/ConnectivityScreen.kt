package com.example.carsetting.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carsetting.settings.R
import com.example.carsetting.model.ConnectivityIntent
import com.example.carsetting.ui.components.NavigationEntryCard
import com.example.carsetting.ui.components.ToggleSettingCard
import com.example.carsetting.viewmodel.ConnectivityViewModel

@Composable
fun ConnectivitySettingsTab(
    viewModel: ConnectivityViewModel = viewModel(factory = ConnectivityViewModel.Factory)
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
            NavigationEntryCard(
                title = stringResource(R.string.bt_audio),
                description = stringResource(R.string.bt_audio_desc),
                icon = Icons.Default.Bluetooth,
                iconTint = Color(0xFF2196F3),
                onClick = { viewModel.handleIntent(ConnectivityIntent.OpenBluetoothSettings) }
            )
        }
        
        item {
            ToggleSettingCard(
                title = stringResource(R.string.wifi_title),
                icon = Icons.Default.Wifi,
                description = stringResource(R.string.wifi_desc),
                checked = state.wifiEnabled,
                onCheckedChange = { viewModel.handleIntent(ConnectivityIntent.ToggleWifi(it)) }
            )
        }

        item {
            NavigationEntryCard(
                title = stringResource(R.string.music_title),
                description = state.currentMusicInfo,
                icon = Icons.Default.MusicNote,
                iconTint = Color(0xFF1DB954),
                onClick = { viewModel.handleIntent(ConnectivityIntent.OpenMusicPlayer) }
            )
        }
        
        item {
            NavigationEntryCard(
                title = stringResource(R.string.projection_title),
                description = stringResource(R.string.projection_desc),
                icon = Icons.Default.Cast,
                iconTint = MaterialTheme.colorScheme.primary,
                onClick = { viewModel.handleIntent(ConnectivityIntent.OpenProjectionSettings) }
            )
        }
    }
}
