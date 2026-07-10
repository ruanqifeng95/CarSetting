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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.carsetting.settings.R
import com.example.carsetting.ui.components.SettingCard
import com.example.carsetting.ui.components.NavigationEntryCard
import com.example.carsetting.util.LocaleManager
import java.util.Locale

@Composable
fun GeneralSettingsTab() {
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
            val currentLang = if (LocaleManager.currentLocale.language == "en") {
                stringResource(R.string.lang_en)
            } else {
                stringResource(R.string.lang_zh)
            }
            
            SettingCard(
                title = stringResource(R.string.language_title),
                icon = Icons.Default.Language,
                options = listOf(stringResource(R.string.lang_zh), stringResource(R.string.lang_en)),
                selectedOption = currentLang,
                onOptionSelected = { selected ->
                    val newLocale = if (selected == context.getString(R.string.lang_en)) {
                        Locale.ENGLISH
                    } else {
                        Locale.SIMPLIFIED_CHINESE
                    }
                    LocaleManager.setLocale(newLocale)
                }
            )
        }

        item {
            var unit by remember { mutableStateOf("公制") }
            val unitOptions = listOf(stringResource(R.string.unit_metric), stringResource(R.string.unit_imperial))
            
            SettingCard(
                title = stringResource(R.string.unit_title),
                icon = Icons.Default.Straighten,
                options = unitOptions,
                selectedOption = if (unit == "公制") unitOptions[0] else unitOptions[1],
                onOptionSelected = { unit = if (it == unitOptions[0]) "公制" else "英制" }
            )
        }

        item {
            NavigationEntryCard(
                title = stringResource(R.string.sys_info),
                description = stringResource(R.string.version_label),
                icon = Icons.Default.Info,
                onClick = { /* Handle about click */ }
            )
        }
        
        item {
            NavigationEntryCard(
                title = stringResource(R.string.factory_reset),
                description = stringResource(R.string.reset_desc),
                icon = Icons.Default.Settings,
                onClick = { /* Handle reset click */ }
            )
        }
    }
}
