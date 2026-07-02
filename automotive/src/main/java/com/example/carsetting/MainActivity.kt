package com.example.carsetting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import com.example.carsetting.navigation.CarNavGraph
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarSettingsTheme {
                CarSettingsScreen()
            }
        }
    }
}

@Composable
fun CarSettingsScreen() {
    val navController = rememberNavController()
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isMainScreen = currentRoute != "battery_preservation"

    val selectedTab = pagerState.currentPage

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "车辆设置",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        scope.launch {
                            if (!isMainScreen) navController.popBackStack()
                            pagerState.animateScrollToPage(
                                page = 0,
                                animationSpec = tween(durationMillis = 800)
                            )
                        }
                    },
                    text = { Text("驾驶") },
                    icon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        scope.launch {
                            if (!isMainScreen) navController.popBackStack()
                            pagerState.animateScrollToPage(
                                page = 1,
                                animationSpec = tween(durationMillis = 800)
                            )
                        }
                    },
                    text = { Text("舒适") },
                    icon = { Icon(Icons.Default.Air, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = {
                        scope.launch {
                            if (!isMainScreen) navController.popBackStack()
                            pagerState.animateScrollToPage(
                                page = 2,
                                animationSpec = tween(durationMillis = 800)
                            )
                        }
                    },
                    text = { Text("安全") },
                    icon = { Icon(Icons.Default.Security, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = {
                        scope.launch {
                            if (!isMainScreen) navController.popBackStack()
                            pagerState.animateScrollToPage(
                                page = 3,
                                animationSpec = tween(durationMillis = 500)
                            )
                        }
                    },
                    text = { Text("互联") },
                    icon = { Icon(Icons.Default.Link, contentDescription = null) }
                )
            }

            // Content Area
            CarNavGraph(
                navController = navController,
                pagerState = pagerState,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCarSettingsScreen() {
    CarSettingsTheme {
        CarSettingsScreen()
    }
}

@Composable
fun CarSettingsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF64B5F6),
            primaryContainer = Color(0xFF1976D2),
            secondary = Color(0xFF81C784),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            surfaceVariant = Color(0xFF2C2C2C),
            onPrimary = Color.White,
            onPrimaryContainer = Color.White,
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceVariant = Color(0xFFB0B0B0)
        ),
        content = content
    )
}
