package com.example.carsetting.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.carsetting.ui.screens.*

@Composable
fun CarNavGraph(
    navController: NavHostController,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable("main") {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> DrivingSettingsTab(onNavigateToBatteryPreservation = {
                        navController.navigate("battery_preservation")
                    })
                    1 -> ComfortSettingsTab()
                    2 -> SafetySettingsTab()
                    3 -> ConnectivitySettingsTab()
                }
            }
        }
        composable("battery_preservation") {
            BatteryPreservationScreen(onBack = { navController.popBackStack() })
        }
    }
}
