package com.example.carsetting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carsetting.model.DrivingIntent
import com.example.carsetting.model.DrivingMode
import com.example.carsetting.model.DrivingSettingsState
import com.example.carsetting.viewmodel.DrivingSettingsViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.carsetting.navigation.CarNavGraph

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
    var selectedTab by remember { mutableIntStateOf(0) }

    // Sync selectedTab with navigation backstack
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            selectedTab = when (route) {
                "driving", "battery_preservation" -> 0
                "comfort" -> 1
                "safety" -> 2
                else -> 0
            }
        }
    }

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

            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        if (selectedTab != 0) {
                            navController.navigate("driving") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    text = { Text("驾驶") },
                    icon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        if (selectedTab != 1) {
                            navController.navigate("comfort") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    text = { Text("舒适") },
                    icon = { Icon(Icons.Default.Air, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = {
                        if (selectedTab != 2) {
                            navController.navigate("safety") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    text = { Text("安全") },
                    icon = { Icon(Icons.Default.Security, contentDescription = null) }
                )
            }

            CarNavGraph(
                navController = navController,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DrivingSettingsTab(
    viewModel: DrivingSettingsViewModel = viewModel(),
    onNavigateToBatteryPreservation: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            DrivingModeCard(
                state = state,
                onIntent = { viewModel.handleIntent(it) }
            )
        }

        item {
            BatteryPreservationEntryCard(onClick = onNavigateToBatteryPreservation)
        }
        
        item {
            AutoStartStopCard(
                state = state,
                onIntent = { viewModel.handleIntent(it) }
            )
        }
        
        item {
            EnergyRecoveryCard(
                state = state,
                onIntent = { viewModel.handleIntent(it) }
            )
        }
        
        item {
            SteeringEffortCard(
                state = state,
                onIntent = { viewModel.handleIntent(it) }
            )
        }
    }
}

@Composable
fun DrivingModeCard(
    state: DrivingSettingsState,
    onIntent: (DrivingIntent) -> Unit
) {
    val options = listOf(
        DrivingMode.Eco,
        DrivingMode.Standard,
        DrivingMode.Sport,
        DrivingMode.OffRoad
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "驾驶模式",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { mode ->
                    FilterChip(
                        selected = state.drivingMode == mode,
                        onClick = { onIntent(DrivingIntent.ChangeDrivingMode(mode)) },
                        label = { Text(mode.displayName) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun BatteryPreservationEntryCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BatteryChargingFull,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = "行车保电",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "设置混合动力系统的保电策略",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
fun BatteryPreservationScreen(onBack: () -> Unit) {
    var preservationMode by remember { mutableIntStateOf(0) } // 0: 智能, 1: 强制
    var targetSoc by remember { mutableFloatStateOf(50f) }
    val isSocEnabled = preservationMode == 1

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

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("保电模式", fontWeight = FontWeight.Medium, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = preservationMode == 0,
                        onClick = { preservationMode = 0 },
                        label = { Text("智能保电") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = preservationMode == 1,
                        onClick = { preservationMode = 1 },
                        label = { Text("强制保电") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .alpha(if (isSocEnabled) 1f else 0.5f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "目标电量",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = if (isSocEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${targetSoc.toInt()}%",
                        color = if (isSocEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Slider(
                    value = targetSoc,
                    onValueChange = { targetSoc = it },
                    valueRange = 20f..80f,
                    steps = 5,
                    enabled = isSocEnabled,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = if (isSocEnabled) "强制保电下，系统将尽量维持电量在设定值附近" else "智能保电下，系统将自动管理电量，无需手动设置",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AutoStartStopCard(
    state: DrivingSettingsState,
    onIntent: (DrivingIntent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = "自动启停",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "在停车时自动关闭发动机",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Switch(
                checked = state.autoStartStop,
                onCheckedChange = { onIntent(DrivingIntent.ToggleAutoStartStop(it)) },
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun EnergyRecoveryCard(
    state: DrivingSettingsState,
    onIntent: (DrivingIntent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BatteryChargingFull,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = "能量回收",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "减速时回收能量",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Switch(
                checked = state.energyRecovery,
                onCheckedChange = { onIntent(DrivingIntent.ToggleEnergyRecovery(it)) },
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun SteeringEffortCard(
    state: DrivingSettingsState,
    onIntent: (DrivingIntent) -> Unit
) {
    val labels = listOf("轻便", "标准", "运动")
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.RotateRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "转向力度",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Slider(
                value = state.steeringEffort.toFloat(),
                onValueChange = { onIntent(DrivingIntent.ChangeSteeringEffort(it.toInt())) },
                valueRange = 1f..3f,
                steps = 1,
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = labels.getOrNull(state.steeringEffort - 1) ?: "",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun ComfortSettingsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ToggleSettingCard(
                title = "空调自动模式",
                icon = Icons.Default.AcUnit,
                description = "自动调节温度和风量"
            )
        }
        
        item {
            SliderSettingCard(
                title = "座椅温度",
                icon = Icons.Default.Whatshot,
                valueRange = 0..3,
                labels = listOf("关闭", "低", "中", "高")
            )
        }
        
        item {
            ToggleSettingCard(
                title = "氛围灯",
                icon = Icons.Default.Lightbulb,
                description = "车内氛围照明"
            )
        }
        
        item {
            SettingCard(
                title = "音量设置",
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                options = listOf("低", "中", "高")
            )
        }
    }
}

@Composable
fun SafetySettingsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ToggleSettingCard(
                title = "盲点监测",
                icon = Icons.Default.Visibility,
                description = "监测盲区车辆"
            )
        }
        
        item {
            ToggleSettingCard(
                title = "车道保持辅助",
                icon = Icons.Default.Straighten,
                description = "帮助保持在车道内"
            )
        }
        
        item {
            ToggleSettingCard(
                title = "碰撞预警",
                icon = Icons.Default.Warning,
                description = "前方碰撞警告"
            )
        }
        
        item {
            ToggleSettingCard(
                title = "自动紧急制动",
                icon = Icons.Default.CarCrash,
                description = "检测到危险时自动刹车"
            )
        }
    }
}

@Composable
fun SettingCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    options: List<String>
) {
    var selectedOption by remember { mutableStateOf(options[0]) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { option ->
                    FilterChip(
                        selected = selectedOption == option,
                        onClick = { selectedOption = option },
                        label = { Text(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ToggleSettingCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String
) {
    var enabled by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Switch(
                checked = enabled,
                onCheckedChange = { enabled = it },
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun SliderSettingCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    valueRange: IntRange,
    labels: List<String>
) {
    var sliderPosition by remember { mutableFloatStateOf(valueRange.first.toFloat()) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
                steps = valueRange.last - valueRange.first - 1,
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = labels.getOrNull((sliderPosition - valueRange.first).toInt()) ?: "",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
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