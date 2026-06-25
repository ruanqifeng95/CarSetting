package com.example.carsetting.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.carsetting.*
import com.example.carsetting.viewmodel.DrivingSettingsViewModel

@Composable
fun CarNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "driving",
        modifier = modifier
    ) {
        composable("driving") {
            val viewModel: DrivingSettingsViewModel = viewModel()
            DrivingSettingsTab(
                viewModel = viewModel,
                onNavigateToBatteryPreservation = {
                    navController.navigate("battery_preservation")
                }
            )
        }
        composable("comfort") { ComfortSettingsTab() }
        composable("safety") { SafetySettingsTab() }
        composable("battery_preservation") {
            BatteryPreservationScreen(onBack = { navController.popBackStack() })
        }
    }
}
