package com.example.carsetting.util

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

object LocaleManager {
    // Start with a safe constant and initialize at runtime to avoid static field warning
    var currentLocale: Locale by mutableStateOf(Locale.SIMPLIFIED_CHINESE)
        private set

    /**
     * Initializes the manager with the initial locale.
     */
    fun init(locale: Locale) {
        currentLocale = locale
        Locale.setDefault(locale)
    }

    /**
     * Updates the app's current locale.
     */
    fun setLocale(locale: Locale) {
        currentLocale = locale
        Locale.setDefault(locale)
    }
}

val LocalAppLocale = compositionLocalOf { Locale.getDefault() }

@Composable
fun LocaleAwareContent(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val currentLocale = LocaleManager.currentLocale
    
    // Create a new context with the overridden configuration
    // Use LocalConfiguration.current instead of context.resources.configuration
    val localeContext = remember(currentLocale, config) {
        val newConfig = Configuration(config).apply {
            setLocale(currentLocale)
            setLayoutDirection(currentLocale)
        }
        context.createConfigurationContext(newConfig)
    }
    
    CompositionLocalProvider(
        LocalContext provides localeContext,
        LocalConfiguration provides localeContext.resources.configuration,
        LocalAppLocale provides currentLocale
    ) {
        content()
    }
}
