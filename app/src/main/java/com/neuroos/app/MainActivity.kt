package com.neuroos.app

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ripple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.camera.view.PreviewView
import androidx.camera.view.LifecycleCameraController
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import android.media.projection.MediaProjectionManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.IconButton
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroOsPrototypeApp()
        }
    }
}


private enum class AppView(val label: String, val key: String, val adultIcon: Int, val kidsIcon: Int) {
    Home("Home", "H", R.drawable.neuro_nav_home, R.drawable.neuro_kids_home),
    Overview("Overview", "O", R.drawable.neuro_nav_overview, R.drawable.neuro_kids_home),
    Launcher("Apps", "A", R.drawable.neuro_nav_apps, R.drawable.neuro_kids_apps),
    Dashboard("Data", "D", R.drawable.neuro_nav_data, R.drawable.neuro_kids_trophy),
    Planner("Plan", "L", R.drawable.neuro_nav_plan, R.drawable.neuro_kids_plan),
    Stickers("Rewards", "K", R.drawable.neuro_nav_rewards, R.drawable.neuro_kids_rewards),
    Routines("Routines", "U", R.drawable.neuro_nav_routines, R.drawable.neuro_kids_routines),
    Talk("Talk", "V", R.drawable.neuro_nav_talk, R.drawable.neuro_kids_talk),
    Finances("Money", "M", R.drawable.neuro_nav_money, R.drawable.neuro_kids_rewards),
    Focus("Focus", "F", R.drawable.neuro_nav_focus, R.drawable.neuro_kids_focus),
    Sensory("Sensory", "S", R.drawable.neuro_nav_sensory, R.drawable.neuro_kids_sensory),
    Themes("Themes", "T", R.drawable.neuro_nav_themes, R.drawable.neuro_kids_home),
    GuardianCall("Support", "G", R.drawable.neuro_nav_support, R.drawable.neuro_kids_support),
    GuardianAI("Insights", "I", R.drawable.neuro_nav_insights, R.drawable.neuro_kids_support),
    MemorySetup("Brain", "B", R.drawable.neuro_nav_brain, R.drawable.neuro_kids_home),
    Profile("Profile", "P", R.drawable.neuro_nav_profile, R.drawable.neuro_kids_profile),
    Reflection("Reflect", "R", R.drawable.neuro_nav_reflect, R.drawable.neuro_kids_sleep),
    About("About", "Q", R.drawable.neuro_logo_primary, R.drawable.neuro_logo_primary)
}

@Composable
fun NeuroOsPrototypeApp() {
    val context = LocalContext.current
    val storage = remember { NeuroStorage(context) }
    val sensoryController = remember { SensoryController(context) }
    val speechManager = remember { SpeechManager(context) }
    val billingManager = remember { BillingManager(context) }
    
    DisposableEffect(Unit) {
        onDispose {
            speechManager.shutdown()
        }
    }
    val billingPremium by billingManager.isPremium.collectAsState()
    var profile by remember { mutableStateOf(storage.loadProfile()) }
    
    // Sync billing state to profile
    LaunchedEffect(billingPremium) {
        if (billingPremium != profile.isPremium) {
            profile = profile.copy(isPremium = billingPremium)
            storage.saveProfile(profile)
        }
    }
    var view by remember { mutableStateOf(AppView.Home) }
    var selectedTaskId by remember { mutableStateOf(NeuroRepository.tasks.first().id) }
    val selectedTask = NeuroRepository.tasks.first { it.id == selectedTaskId }
    var session by remember { mutableStateOf(createFocusSession(selectedTask)) }
    var energyLogs by remember { mutableStateOf(
        listOf(
            EnergyEntry(3), EnergyEntry(5), EnergyEntry(4), 
            EnergyEntry(2), EnergyEntry(4), EnergyEntry(5)
        )
    ) }

    fun updateProfile(nextProfile: NeuroProfile) {
        profile = nextProfile
        storage.saveProfile(nextProfile)
    }

    fun speak(text: String) {
        if (profile.speechEnabled) {
            speechManager.speak(text)
        }
    }

    fun buySticker(sticker: Sticker) {
        if (profile.tokens >= 50 && !profile.stickers.contains(sticker.id)) {
            updateProfile(profile.copy(
                tokens = profile.tokens - 50,
                stickers = profile.stickers + sticker.id
            ))
            speak("You got a new sticker: ${sticker.name}!")
        }
    }

    fun claimRobux() {
        if (profile.tokens >= 100) {
            updateProfile(profile.copy(
                tokens = profile.tokens - 100,
                robuxBalance = profile.robuxBalance + 10
            ))
            speak("10 Robux added to your balance!")
        }
    }

    LaunchedEffect(session.status) {
        if (session.status == SessionStatus.Transitioning) {
            speak("Transition period starting. Take a deep breath.")
        } else if (session.status == SessionStatus.Active) {
            speak("Focus session started. Let's work on ${session.taskTitle}.")
        } else if (session.status == SessionStatus.Complete) {
            speak("Great job! Session complete. You've earned tokens!")
        }
    }

    // Audio cue for transition end
    LaunchedEffect(session.transitionSeconds) {
        if (session.status == SessionStatus.Transitioning && session.transitionSeconds == 5) {
            speak("Five seconds left. Ready to begin?")
        }
    }

    fun selectTask(task: StudyTask) {
        selectedTaskId = task.id
        session = createFocusSession(task)
    }

    fun logEnergy(level: Int) {
        val nextLogs = energyLogs + EnergyEntry(level)
        energyLogs = nextLogs
        storage.saveEnergyLogs(nextLogs)
    }

    fun logUsage(packageName: String) {
        val calendar = java.util.Calendar.getInstance()
        val day = calendar.get(java.util.Calendar.DAY_OF_WEEK)
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        
        val existing = profile.usageLogs.find { it.packageName == packageName && it.dayOfWeek == day && it.hourOfDay == hour }
        val nextLogs = if (existing != null) {
            profile.usageLogs.map { if (it == existing) it.copy(count = it.count + 1) else it }
        } else {
            profile.usageLogs + UsageLog(packageName, day, hour)
        }
        updateProfile(profile.copy(usageLogs = nextLogs))
    }

    fun completeStep() {
        if (session.activeStepIndex < session.totalSteps) {
            session = reduceSession(session, SessionAction.CompleteStep)
            // Reward dopamine hit with tokens
            updateProfile(profile.copy(tokens = profile.tokens + 10))
        }
    }

    LaunchedEffect(session.status, session.remainingSeconds, session.transitionSeconds) {
        if ((session.status == SessionStatus.Active && session.remainingSeconds > 0) ||
            (session.status == SessionStatus.Transitioning && session.transitionSeconds > 0)
        ) {
            delay(1000)
            session = reduceSession(session, SessionAction.Tick())
        }
    }

    var showPaywall by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = colorSchemeFor(profile, isSystemInDarkTheme())) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .safeDrawingPadding()
        ) {
            if (showPaywall) {
                PaywallScreen(
                    onSubscribeMonthly = { 
                        billingManager.launchPurchaseFlow(context as Activity, isLifetime = false)
                        showPaywall = false
                    },
                    onBuyLifetime = { 
                        billingManager.launchPurchaseFlow(context as Activity, isLifetime = true)
                        showPaywall = false
                    },
                    onClose = { showPaywall = false }
                )
            } else if (profile.themeMode == AppThemeMode.Kids && (view == AppView.Home || view == AppView.Dashboard)) {
                KidsDashboard(
                    profile = profile,
                    onViewChange = { view = it },
                    onStartSession = {
                        session = reduceSession(createFocusSession(selectedTask), SessionAction.StartTransition())
                        view = AppView.Focus
                    },
                    session = session,
                    energyLogs = energyLogs,
                    onLogEnergy = ::logEnergy,
                    onProfileChange = ::updateProfile
                )
            } else if (profile.themeMode == AppThemeMode.Adult && (view == AppView.Home || view == AppView.Dashboard)) {
                AdultDashboard(
                    profile = profile,
                    onViewChange = { view = it },
                    onStartSession = {
                        session = reduceSession(createFocusSession(selectedTask), SessionAction.StartTransition())
                        view = AppView.Focus
                    },
                    session = session,
                    energyLogs = energyLogs,
                    onLogEnergy = ::logEnergy,
                    onProfileChange = ::updateProfile
                )
            } else {
                Row(Modifier.fillMaxSize()) {
                    NeuroNavigationRail(
                        current = view,
                        onSelect = { view = it },
                        profile = profile
                    )

                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        // High-fidelity circuit backgrounds
                        if (profile.sensoryProfile.id == "calm" && profile.themeMode != AppThemeMode.Kids) {
                            val bgRes = when(view) {
                                AppView.Home -> R.drawable.bg_neuro_circuit_01
                                AppView.Overview, AppView.Dashboard -> R.drawable.bg_neuro_circuit_02
                                AppView.Focus -> R.drawable.bg_neuro_circuit_03
                                else -> null
                            }
                            CyberBackgroundPattern(modifier = Modifier.matchParentSize(), backgroundRes = bgRes)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TopBar(view = view, profile = profile, session = session)

                            Box(modifier = Modifier.weight(1f)) {
                                when (view) {
                                    AppView.Overview -> OverviewScreen()
                                    AppView.Home -> HomeScreen(
                                        selectedTask = selectedTask,
                                        onStart = {
                                            session = reduceSession(createFocusSession(selectedTask), SessionAction.StartTransition())
                                            view = AppView.Focus
                                        },
                                        onSelectTask = ::selectTask,
                                        speak = ::speak,
                                        profile = profile,
                                        onNavigate = { view = it }
                                    )

                                    AppView.Launcher -> LauncherScreen(
                                        session = session, 
                                        profile = profile,
                                        onLaunchApp = { logUsage(it) }
                                    )

                                    AppView.Dashboard -> DashboardScreen(
                                        energyLogs = energyLogs,
                                        onLogEnergy = ::logEnergy,
                                        speak = ::speak
                                    )

                                    AppView.Planner -> PlannerScreen(
                                    speak = ::speak, 
                                    isPremium = profile.isPremium, 
                                    onGoPremium = { showPaywall = true }
                                )

                                AppView.Stickers -> StickerBookScreen(
                                    profile = profile,
                                    onBuySticker = ::buySticker,
                                    onClaimRobux = { claimRobux() },
                                    speak = ::speak
                                )

                                AppView.Routines -> RoutineBoardScreen(
                                    onEarnTokens = { updateProfile(profile.copy(tokens = profile.tokens + 10)) },
                                    speak = ::speak,
                                    isPremium = profile.isPremium,
                                    onGoPremium = { showPaywall = true }
                                )

                                AppView.Talk -> CommunicationBoardScreen(
                                    speak = ::speak, 
                                    isPremium = profile.isPremium, 
                                    onGoPremium = { showPaywall = true }
                                )

                                    AppView.Finances -> FinanceScreen(speak = ::speak)

                                    AppView.Focus -> FocusScreen(
                                        task = selectedTask,
                                        session = session,
                                        onStartOrResume = {
                                            session = reduceSession(
                                                session,
                                                if (session.status == SessionStatus.Ready) SessionAction.Start() else SessionAction.Resume
                                            )
                                        },
                                        onPause = { session = reduceSession(session, SessionAction.Pause) },
                                        onCompleteStep = { completeStep() },
                                        onCompleteSession = {
                                            session = reduceSession(session, SessionAction.CompleteSession())
                                            view = AppView.Reflection
                                        },
                                        onOpenSensory = { view = AppView.Sensory },
                                        onReflect = { view = AppView.Reflection },
                                        onGameReward = { updateProfile(profile.copy(tokens = profile.tokens + 50)) },
                                        speak = ::speak
                                    )

                                    AppView.Sensory -> SensoryScreen(
                                        profile = profile,
                                        onProfileChange = ::updateProfile,
                                        controller = sensoryController
                                    )

                                    AppView.Themes -> ThemeMatrixScreen(profile = profile, onProfileChange = ::updateProfile)

                                    AppView.GuardianCall -> {
                                    if (profile.isPremium) GuardianCallScreen(onClose = { view = AppView.Home }, isPremium = true, onGoPremium = {})
                                    else showPaywall = true
                                }

                                AppView.GuardianAI -> {
                                    if (profile.isPremium) GuardianAiScreen(profile = profile, energyLogs = energyLogs, isPremium = true, onGoPremium = {})
                                    else showPaywall = true
                                }

                                    AppView.MemorySetup -> MemoryNeedsScreen(
                                        profile = profile,
                                        onUpdateNeed = { id, enabled ->
                                            val nextNeeds = profile.memoryNeeds.map { 
                                                if (it.id == id) it.copy(enabled = enabled) else it 
                                            }
                                            updateProfile(profile.copy(memoryNeeds = nextNeeds))
                                        }
                                    )

                                    AppView.Profile -> ProfileScreen(profile = profile, onProfileChange = ::updateProfile)
                                    AppView.Reflection -> ReflectionScreen(
                                        session = session,
                                        onSave = {
                                            session = reduceSession(session, SessionAction.Reflect(it))
                                        }
                                    )
                                    AppView.About -> AboutScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
private fun NeuroNavigationRail(current: AppView, onSelect: (AppView) -> Unit, profile: NeuroProfile) {
    val isKids = profile.themeMode == AppThemeMode.Kids
    val containerColor = if (isKids) Color(0xFF0A192F) else Color(0xFF050811)
    
    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .width(if (isKids) 110.dp else 96.dp),
        containerColor = containerColor,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .testTag("nav_rail_column"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            
            // Fixed branding position at the top
            Image(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.neuro_logo_primary),
                contentDescription = "NeuroOS Branding",
                modifier = Modifier
                    .size(if (isKids) 64.dp else 52.dp)
                    .padding(4.dp)
            )
            
            Spacer(Modifier.height(24.dp))

            val displayedItems = if (isKids) {
                listOf(
                    AppView.Home, AppView.Talk, AppView.Routines, AppView.Launcher, 
                    AppView.Stickers, AppView.Planner, AppView.Focus, AppView.Sensory, 
                    AppView.GuardianCall, AppView.Reflection, AppView.Profile
                )
            } else {
                AppView.entries
            }

            displayedItems.forEach { item ->
                val isSelected = current == item
                NavigationRailItem(
                    selected = isSelected,
                    onClick = { onSelect(item) },
                    modifier = Modifier
                        .heightIn(min = if (isKids) 84.dp else 72.dp)
                        .testTag("nav_${item.name}")
                        .semantics { contentDescription = item.label },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(if (isKids) 48.dp else 40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.22f)
                                    else Color.Transparent
                                )
                                .then(
                                    if (isSelected) Modifier.border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = androidx.compose.ui.res.painterResource(id = if (isKids) item.kidsIcon else item.adultIcon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(if (isKids) 36.dp else 28.dp)
                                    .alpha(if (isSelected) 1f else 0.72f)
                            )
                        }
                    },
                    label = { 
                        Text(
                            text = if (isKids) item.label.take(8) else item.label, 
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant, 
                            fontSize = if (isKids) 12.sp else 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        ) 
                    },
                    alwaysShowLabel = true
                )
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TopBar(view: AppView, profile: NeuroProfile, session: FocusSession) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // High-fidelity branding in TopBar with Glass-morphism
        Surface(
            modifier = Modifier.size(48.dp).border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shadowElevation = 4.dp
        ) {
            Image(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.neuro_logo_primary),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }
        
        Spacer(Modifier.width(14.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "NeuroOS • ${profile.studentSegment.uppercase()}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Text(
                text = when (view) {
                AppView.Overview -> stringResource(R.string.neuro_overview_title)
                AppView.Home -> "Today"
                    AppView.Launcher -> "Toolbox"
                    AppView.Dashboard -> "NeuroStats"
                    AppView.Planner -> "NeuroPlanner"
                    AppView.Stickers -> "Reward Shop"
                    AppView.Routines -> "Daily Routines"
                    AppView.Talk -> "Talk Board"
                    AppView.Finances -> "Executive Finance"
                    AppView.Focus -> "FocusFrame"
                    AppView.Sensory -> "SensoryShield"
                    AppView.Themes -> "ThemeMatrix"
                    AppView.GuardianCall -> "Guardian Link"
                    AppView.GuardianAI -> "AI Guardian"
                    AppView.MemorySetup -> "External Brain"
                    AppView.Profile -> "NeuroProfile"
                    AppView.Reflection -> "Reflection"
                    AppView.About -> "Our Mission"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp), 
                horizontalArrangement = Arrangement.spacedBy(8.dp), 
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusPill(profile.sensoryProfile.label)
                StatusPill(session.status.name.lowercase())
                
                Spacer(Modifier.weight(1f))
                
                // Neuro-Token Counter
                Surface(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(end = 4.dp),
                ) {
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("✧", color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Black)
                        Spacer(Modifier.width(4.dp))
                        Text(profile.tokens.toString(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusPill(label: String) {
    Text(
        text = label,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        maxLines = 1,
        softWrap = false
    )
}

@Composable
fun PlannerScreen(speak: (String) -> Unit, isPremium: Boolean, onGoPremium: () -> Unit) {
    val items = NeuroRepository.weeklyPlanner
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    
    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("Energy-Aware Weekly Plan")
        
        days.forEachIndexed { index, day ->
            val isLocked = index > 1 && !isPremium
            val dayItems = items.filter { it.day == day }
            val totalEnergy = dayItems.sumOf { it.energyCost }
            
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.alpha(if (isLocked) 0.4f else 1f)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(day, modifier = Modifier.clickable { if (!isLocked) speak(day) }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        if (isLocked) Text("🔒", fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Day Energy:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("🔋" + "⚡".repeat(totalEnergy), fontSize = 12.sp)
                    }
                }
                
                if (isLocked) {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(80.dp).clickable(onClick = onGoPremium),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Unlock Full Week with Premium", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    dayItems.forEach { item ->
                        PlannerCard(item, speak)
                    }
                    
                    if (dayItems.isEmpty()) {
                        Text(
                            text = "No tasks scheduled.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlannerCard(item: PlannerItem, speak: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { speak(item.title) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        when(item.energyCost) {
                            in 1..2 -> Color(0xFF46E6A5) // Success Green
                            3 -> Color(0xFFFFD54F) // Warning Gold
                            else -> Color(0xFFFF647C) // Error Red
                        }
                    )
            )
            
            Column(Modifier.weight(1f)) {
                Text(item.title, fontWeight = FontWeight.Bold)
                Text("Cost: ${item.energyCost} energy units", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            
            if (item.completed) {
                Text("✅", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun CommunicationBoardScreen(speak: (String) -> Unit, isPremium: Boolean, onGoPremium: () -> Unit) {
    val cards = CommunicationRepository.needs
    
    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("Talk with Pictures")
        Text(
            text = "Tap a picture to hear the word and sound!", 
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            cards.forEachIndexed { index, card ->
                val isLocked = index >= 4 && !isPremium
                CommunicationItem(card, speak, isLocked, onGoPremium)
            }
        }
    }
}

@Composable
fun CommunicationItem(card: CommunicationCard, speak: (String) -> Unit, isLocked: Boolean, onGoPremium: () -> Unit) {
    Card(
        modifier = Modifier
            .size(140.dp)
            .clickable { 
                if (isLocked) onGoPremium()
                else speak("${card.phonics}... ${card.label}") 
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLocked) 0.dp else 4.dp)
    ) {
        Column(
            Modifier.fillMaxSize().padding(12.dp).alpha(if (isLocked) 0.4f else 1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLocked) {
                Text("🔒", fontSize = 48.sp)
                Text("Premium", style = MaterialTheme.typography.labelSmall)
            } else {
                Text(card.icon, fontSize = 64.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = card.label,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun RoutineBoardScreen(onEarnTokens: () -> Unit, speak: (String) -> Unit, isPremium: Boolean, onGoPremium: () -> Unit) {
    val boards = listOf(RoutineRepository.pottyTraining, RoutineRepository.morningSpark)
    var activeBoard by remember { mutableStateOf(boards.first()) }
    var completedSteps by remember { mutableStateOf(setOf<String>()) }

    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("Velcro Routine Board")
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            boards.forEachIndexed { index, board ->
                val isBoardLocked = index > 0 && !isPremium
                FilterChip(
                    selected = activeBoard.id == board.id,
                    onClick = { 
                        if (isBoardLocked) onGoPremium()
                        else {
                            activeBoard = board
                            completedSteps = emptySet()
                        }
                    },
                    label = { 
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(board.title)
                            if (isBoardLocked) Text("🔒", fontSize = 10.sp)
                        }
                    }
                )
            }
        }

        activeBoard.steps.forEach { step ->
            val isDone = completedSteps.contains(step.id)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
                    .clickable { 
                        if (!isDone) {
                            completedSteps = completedSteps + step.id
                            onEarnTokens()
                            speak("Good job! You finished ${step.title}")
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (isDone) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(2.dp, if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    Modifier.fillMaxSize().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(step.icon, fontSize = 32.sp)
                    Text(
                        step.title, 
                        Modifier.weight(1f), 
                        fontWeight = FontWeight.Bold,
                        color = if (isDone) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                    if (isDone) Text("✅", fontSize = 24.sp)
                    else Text("🔘", fontSize = 24.sp, modifier = Modifier.alpha(0.3f))
                }
            }
        }
    }
}

@Composable
fun StickerBookScreen(
    profile: NeuroProfile,
    onBuySticker: (Sticker) -> Unit,
    onClaimRobux: (Int) -> Unit, // Using Int just to keep signature simple
    speak: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("Your Reward Collection")
        
        // Balance Cards
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Tokens", "✧${profile.tokens}", Modifier.weight(1f))
            StatCard("Robux", "R$ ${profile.robuxBalance}", Modifier.weight(1f))
        }

        Button(
            onClick = { onClaimRobux(10) },
            enabled = profile.tokens >= 100,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exchange 100 Tokens for 10 Robux")
        }

        SectionTitle("Sticker Shop (50 ✧ each)")
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeuroRepository.stickerCatalog.forEachIndexed { index, sticker ->
                val isLocked = index >= 2 && !profile.isPremium
                val owned = profile.stickers.contains(sticker.id)
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { 
                            if (isLocked) { /* handled by visual hint */ }
                            else if (!owned) onBuySticker(sticker) 
                            else speak("You already have ${sticker.name}")
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            owned -> MaterialTheme.colorScheme.primaryContainer
                            isLocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ),
                    border = if (!owned && !isLocked) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (isLocked) {
                            Text("🔒", fontSize = 24.sp)
                            Text(
                                text = "Premium", 
                                style = MaterialTheme.typography.labelSmall, 
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(sticker.emoji, fontSize = 40.sp, modifier = Modifier.alpha(if (owned) 1f else 0.3f))
                            Text(sticker.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                            if (owned) Text("OWNED", fontSize = 8.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FinanceScreen(speak: (String) -> Unit) {
    val items = NeuroRepository.adultFinances
    val totalIncome = items.filter { it.type == "income" }.sumOf { it.amount }
    val totalBills = items.filter { it.type == "bill" }.sumOf { it.amount }
    
    Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("Executive Finance Tracker")
        
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("In Flow", "$$totalIncome", Modifier.weight(1f))
            StatCard("Out Flow", "$$totalBills", Modifier.weight(1f))
        }

        SectionTitle("Upcoming Bills")
        items.filter { it.type == "bill" }.forEach { bill ->
            OutlinedCard(Modifier.fillMaxWidth().clickable { speak("${bill.title} due ${bill.dueDate ?: "soon"}") }) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(bill.title, fontWeight = FontWeight.Bold)
                        Text("Due: ${bill.dueDate ?: "Monthly"}", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(
                        text = "$$${bill.amount}", 
                        fontWeight = FontWeight.Black, 
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(energyLogs: List<EnergyEntry>, onLogEnergy: (Int) -> Unit, speak: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("Neuro-Battery")
        EnergyChart(energyLogs)
        EnergyLogger(onLogEnergy)
        
        SectionTitle("Focus Recovery")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Daily Focus", "${energyLogs.size * 25}m", Modifier.weight(1f).clickable { speak("Your daily focus time is ${energyLogs.size * 25} minutes") })
            StatCard("Avg Ease", "4.2", Modifier.weight(1f).clickable { speak("Your average ease score is 4.2") })
        }

        SectionTitle("Recent Insights")
        InsightCard("Energy peaks at 10 AM", "Schedule high-energy tasks then.", speak)
    }
}

@Composable
fun EnergyLogger(onLogEnergy: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("How is your energy right now?", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                (1..5).forEach { level ->
                    OutlinedButton(
                        onClick = { onLogEnergy(level) },
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text(level.toString(), fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun EnergyChart(logs: List<EnergyEntry>) {
    val displayLogs = logs.takeLast(7).ifEmpty { 
        listOf(EnergyEntry(2), EnergyEntry(3), EnergyEntry(2)) // Placeholders
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(Modifier.padding(20.dp), contentAlignment = Alignment.BottomStart) {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                displayLogs.forEach { entry ->
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight(entry.level / 5f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                    )
                }
            }
        }
    }
}

@Composable
fun InsightCard(title: String, description: String, speak: (String) -> Unit) {
    OutlinedCard(Modifier.fillMaxWidth().clickable { speak("$title. $description") }) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun KidsIslandButton(label: String, iconRes: Int, color: Color, locked: Boolean = false, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 110.dp)
            .graphicsLayer { 
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color.White),
                onClick = onClick
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = if (locked) Color.Gray.copy(alpha = 0.3f) else color),
        elevation = CardDefaults.cardElevation(defaultElevation = if (locked) 0.dp else 12.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            // Subtle inner glow/gradient for high-fidelity feel
            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.2f), Color.Transparent)
                        )
                    )
            )
            
            Row(
                Modifier.padding(24.dp).alpha(if (locked) 0.5f else 1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Image(
                            painter = androidx.compose.ui.res.painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().padding(12.dp)
                        )
                    }
                    Text(
                        text = label, 
                        color = if (locked) Color.Gray else Color.White, 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                }
                if (locked) {
                    Text("🔒", fontSize = 24.sp)
                } else {
                    Text("➔", color = Color.White.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun KidNavIcon(label: String, icon: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 28.sp, modifier = Modifier.alpha(if (selected) 1f else 0.5f))
        Text(label, style = MaterialTheme.typography.labelSmall, color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray)
    }
}

@Composable
private fun AdultDashboard(
    profile: NeuroProfile,
    onViewChange: (AppView) -> Unit,
    onStartSession: () -> Unit,
    session: FocusSession,
    energyLogs: List<EnergyEntry>,
    onLogEnergy: (Int) -> Unit,
    onProfileChange: (NeuroProfile) -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
                // Professional Header
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Executive Dashboard", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Text("Intention for today", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    }
                    Text(
                        text = "Settings",
                        modifier = Modifier.clickable { onProfileChange(profile.copy(themeMode = AppThemeMode.Default)) },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Focus Action
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Row(
                        Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Next Priority", style = MaterialTheme.typography.labelSmall)
                            Text("Deep Work Session", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }
                        Button(onClick = onStartSession, shape = RoundedCornerShape(8.dp)) {
                            Text("Enter Focus")
                        }
                    }
                }

                // Regulation Toolkit (ABA/Psychology based grounding)
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionTitle("Executive Toolkit")
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AdultToolButton("S.O.S Grounding", R.drawable.neuro_nav_sensory, Modifier.weight(1f)) { /* SOS */ }
                        AdultToolButton("My Finances", R.drawable.neuro_nav_money, Modifier.weight(1f)) { onViewChange(AppView.Finances) }
                    }
                }

                // Energy Insight
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Biological Rhythms", fontWeight = FontWeight.Bold)
                        Text(
                            "You usually focus best after a 10-minute sensory reset. Tap below to log current state.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            (1..5).forEach { level ->
                                Text(
                                    level.toString(),
                                    modifier = Modifier
                                        .clickable { onLogEnergy(level) }
                                        .background(
                                            if (energyLogs.lastOrNull()?.level == level) MaterialTheme.colorScheme.primary 
                                            else Color.Transparent, 
                                            CircleShape
                                        )
                                        .padding(8.dp),
                                    fontWeight = FontWeight.Black,
                                    color = if (energyLogs.lastOrNull()?.level == level) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }

@Composable
fun AdultToolButton(label: String, iconRes: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedCard(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = androidx.compose.ui.res.painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(label, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
        }
    }
}

private fun afterglowColorScheme() = darkColorScheme(
    primary = Color(0xFF00E5FF), // Afterglow Cyan
    onPrimary = Color(0xFF050811), // Near-black for buttons
    primaryContainer = Color(0xFF111627), // Elevated Surface
    onPrimaryContainer = Color(0xFFF4F7FF),
    secondary = Color(0xFFA29BFE), // Afterglow Violet
    onSecondary = Color(0xFF0B0E1A),
    secondaryContainer = Color(0xFF171D31), // Interactive Surface
    onSecondaryContainer = Color(0xFFF4F7FF),
    tertiary = Color(0xFFFFD54F), // Accent Gold
    onTertiary = Color(0xFF070A12),
    background = Color(0xFF070A12), // Afterglow Canvas
    surface = Color(0xFF0B0E1A), // Afterglow Surface
    onSurface = Color(0xFFF4F7FF), // Primary Text
    surfaceVariant = Color(0xFF111627), // Elevated Surface
    onSurfaceVariant = Color(0xFFE2E8F0), // Brightened Secondary Text for High Contrast
    outline = Color(0xFF27314D), // Border
    outlineVariant = Color(0xFF1C2439), // Subtle Divider
    error = Color(0xFFFF647C),
    scrim = Color(0xFF7E89A3) // Tertiary Text
)

private fun daylightColorScheme() = lightColorScheme(
    primary = Color(0xFF007F9E), // Primary Cyan
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE7EDF6), // Elevated Surface
    onPrimaryContainer = Color(0xFF111827),
    secondary = Color(0xFF6750A4), // Secondary Violet
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE6F2), // Interactive Surface
    onSecondaryContainer = Color(0xFF111827),
    tertiary = Color(0xFF9A6800), // Accent Gold
    onTertiary = Color.White,
    background = Color(0xFFF2F6FA), // Canvas
    surface = Color.White, // Surface
    onSurface = Color(0xFF111827), // Primary Text
    surfaceVariant = Color(0xFFE7EDF6), // Elevated Surface
    onSurfaceVariant = Color(0xFF526075), // Secondary Text
    outline = Color(0xFFB7C6D9), // Border
    outlineVariant = Color(0xFFD3DDE9), // Subtle Divider
    error = Color(0xFFB4233B),
    scrim = Color(0xFF6E7C91) // Tertiary Text
)


@Composable
private fun KidsDashboard(
    profile: NeuroProfile,
    onViewChange: (AppView) -> Unit,
    onStartSession: () -> Unit,
    session: FocusSession,
    energyLogs: List<EnergyEntry>,
    onLogEnergy: (Int) -> Unit,
    onProfileChange: (NeuroProfile) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Encouragement & Token Bar
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(40.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.4f)) {
                    Image(
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.neuro_logo_primary),
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "${profile.tokens} Tokens!",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Parent Gate: Long press to exit Kids Mode
            Text(
                text = "Parent Mode",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                onProfileChange(profile.copy(themeMode = AppThemeMode.Default))
                            }
                        )
                    }
                    .padding(8.dp)
            )
        }

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Hello Explorer!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black
            )
            
            Text(
                text = "You’re protected and ready to focus!",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

            // Dynamic Adventure Islands
            KidsIslandButton("Mission: Focus", R.drawable.neuro_kids_home, Color(0xFF46E6A5), onClick = onStartSession)
            KidsIslandButton("Talk with Pictures", R.drawable.neuro_kids_talk, Color(0xFFA29BFE)) { onViewChange(AppView.Talk) }
            
            val isPremium = profile.isPremium
            KidsIslandButton("My Routines", R.drawable.neuro_kids_routines, Color(0xFF2979FF), locked = !isPremium) { onViewChange(AppView.Routines) }
            KidsIslandButton("Reward Shop", R.drawable.neuro_kids_rewards, Color(0xFFFFD54F), locked = !isPremium) { onViewChange(AppView.Stickers) }
            KidsIslandButton("Guardian Link", R.drawable.neuro_kids_support, Color(0xFF00E5FF), locked = !isPremium) { onViewChange(AppView.GuardianCall) }

            // Social Emotional Check-in
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            ) {
                Column(
                    Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "How are you feeling right now?",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("😴", "🥱", "😐", "😊", "🔥").forEachIndexed { index, emoji ->
                            Text(
                                text = emoji,
                                fontSize = 40.sp,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { onLogEnergy(index + 1) }
                                    .padding(8.dp)
                            )
                        }
                    }
                    Text(
                        "Guardian is watching over you 🟢",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            
            Spacer(Modifier.height(40.dp))
        }
    }
}

private fun kidsColorScheme() = darkColorScheme(
    primary = Color(0xFF00E5FF),
    onPrimary = Color.Black,
    secondary = Color(0xFFA29BFE),
    onSecondary = Color.Black,
    tertiary = Color(0xFFFFD54F),
    onTertiary = Color.Black,
    background = Color(0xFF0A192F),
    surface = Color(0xFF1E293B),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1), // Light gray for secondary text
    error = Color(0xFFFF5252)
)

@Composable
fun LauncherScreen(session: FocusSession, profile: NeuroProfile, onLaunchApp: (String) -> Unit) {
    val context = LocalContext.current
    val allApps = remember { getInstalledApps(context) }
    
    val displayedApps = remember(session.status, profile.filterAppsInFocus) {
        if (profile.filterAppsInFocus && session.status == SessionStatus.Active) {
            allApps.filter { !isDistracting(it) }
        } else {
            allApps
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(if (session.status == SessionStatus.Active) "Focus Tools" else "Available Tools")
            if (session.status == SessionStatus.Active && profile.filterAppsInFocus) {
                StatusPill("Filtering active")
            }
        }
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            displayedApps.forEach { app ->
                AppIconItem(app) {
                    onLaunchApp(app.packageName)
                    launchApp(context, app.packageName)
                }
            }
        }
    }
}

@Composable
fun AppIconItem(app: AppInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            bitmap = app.icon.toBitmap().asImageBitmap(),
            contentDescription = app.label.toString(),
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        
        Text(
            text = app.label.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MemoryNeedsScreen(profile: NeuroProfile, onUpdateNeed: (String, Boolean) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("Configure your External Brain")
        Text(
            "Which of these do you struggle to remember? I'll proactively suggest them for you.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        profile.memoryNeeds.forEach { need ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (need.enabled) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(need.icon, fontSize = 32.sp)
                    Column(Modifier.weight(1f)) {
                        Text(need.title, fontWeight = FontWeight.Black)
                        Text(need.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Switch(checked = need.enabled, onCheckedChange = { onUpdateNeed(need.id, it) })
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(
    selectedTask: StudyTask,
    onStart: () -> Unit,
    onSelectTask: (StudyTask) -> Unit,
    speak: (String) -> Unit,
    profile: NeuroProfile,
    onNavigate: (AppView) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        // External Brain Suggestion
        val activeNeed = profile.memoryNeeds.find { it.enabled }
        if (activeNeed != null) {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { speak("Don't forget your ${activeNeed.title}") },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f))
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(activeNeed.icon, fontSize = 24.sp)
                    Column(Modifier.weight(1f)) {
                        Text("External Brain Suggestion", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                        Text(activeNeed.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Text("Check", modifier = Modifier.clickable { 
                        if (activeNeed.id == "mn1") onNavigate(AppView.Finances)
                        if (activeNeed.id == "mn2") onNavigate(AppView.Planner)
                    }, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth().clickable { speak("Your current priority is ${selectedTask.title}") }
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Current priority", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(selectedTask.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                Text("Next step: ${selectedTask.steps.first()}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .semantics { contentDescription = "Start study session for ${selectedTask.title}" }
                ) {
                    Text("Start study session", fontWeight = FontWeight.Bold)
                }
            }
        }

        SectionTitle("Daily Flow")
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NeuroRepository.dailySchedule.forEach { block ->
                    FlowBlock(block, speak)
                }
            }
        }

        SectionTitle("Classes")
        NeuroRepository.classes.forEach { studyClass ->
            ClassRow(studyClass, speak)
        }

        SectionTitle("Study tasks")
        NeuroRepository.tasks.forEach { task ->
            TaskRow(
                task = task,
                selected = task.id == selectedTask.id,
                onSelect = { onSelectTask(task) },
                speak = speak
            )
        }
    }
}

@Composable
fun PaywallScreen(onSubscribeMonthly: () -> Unit, onBuyLifetime: () -> Unit, onClose: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("✨", fontSize = 64.sp)
            Text(
                text = "Unlock Everything",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Choose the plan that fits your life. No limits, pure focus.",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(32.dp))
            
            PremiumFeatureRow("Full Kids Adventure Mode", "Every island and dopamine mini-game.")
            PremiumFeatureRow("All AI Insights", "Predictive data to prevent burnout.")
            PremiumFeatureRow("Complete Planner", "The full Energy-Aware weekly stack.")
            PremiumFeatureRow("Priority Guardian Link", "Priority video and screen sharing.")
            
            Spacer(Modifier.height(48.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onSubscribeMonthly,
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Monthly: $1.99 / month", fontWeight = FontWeight.Bold)
                }
                
                OutlinedButton(
                    onClick = onBuyLifetime,
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("Lifetime: $15.00 (One-time)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
            
            TextButton(onClick = { /* Restore */ }) {
                Text("Already a premium member? Restore", style = MaterialTheme.typography.labelSmall)
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) {
            Text("✕", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PremiumFeatureRow(title: String, desc: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("✅", color = MaterialTheme.colorScheme.primary)
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GuardianAiScreen(profile: NeuroProfile, energyLogs: List<EnergyEntry>, isPremium: Boolean, onGoPremium: () -> Unit) {
    val insights = remember(profile.tokens, energyLogs.size) {
        listOf(
            GuardianAiInsight(
                "Focus Pattern Detected",
                "Your child is earning 40% more tokens between 10 AM and 11 AM.",
                "Consider scheduling the hardest IEP tasks during this high-dopamine window.",
                "happy"
            ),
            GuardianAiInsight(
                "Energy Dip Warning",
                "Energy logs show a consistent drop after lunch (around 1:30 PM).",
                "Switch to low-stimulus activities or the 'Sensory Sanctuary' to prevent afternoon meltdowns.",
                "caution"
            ),
            GuardianAiInsight(
                "Social-Emotional Goal",
                "The 'Talk Board' was used 5 times today for 'Water' and 'Help'.",
                "Great progress in self-advocacy! Reward this with a special Dinosaur sticker tonight.",
                "happy"
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("AI Companion Insights")
        Text(
            "Neuro OS AI analyzes focus patterns, energy logs, and talk board usage to help you provide targeted support.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        insights.forEachIndexed { index, insight ->
            val isLocked = index > 0 && !isPremium
            AiInsightCard(insight, isLocked, onGoPremium)
        }
        
        OutlinedButton(
            onClick = { if (isPremium) { /* report */ } else onGoPremium() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Full Weekly IEP Progress Report" + if (!isPremium) " 🔒" else "")
        }
    }
}

@Composable
fun AiInsightCard(insight: GuardianAiInsight, isLocked: Boolean = false, onGoPremium: () -> Unit = {}) {
    val bgColor = when {
        isLocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        insight.emotion == "happy" -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
        insight.emotion == "caution" -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth().clickable { if (isLocked) onGoPremium() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Box {
            // High-fidelity background pulse
            Image(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.neuro_feature_graphic),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(0.1f),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            
            if (isLocked) {
                Column(Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔒 Premium Insight", fontWeight = FontWeight.Bold)
                    Text("Unlock predictive patterns with NeuroOS Premium", style = MaterialTheme.typography.labelSmall)
                }
            } else {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (insight.emotion == "happy") "✨" else "🔍", fontSize = 20.sp)
                        Text(insight.title, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                    }
                    Text(insight.summary, style = MaterialTheme.typography.bodyMedium)
                    Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Gray.copy(alpha = 0.2f)))
                    Text("AI Suggestion:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text(insight.suggestion, style = MaterialTheme.typography.bodySmall, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                }
            }
        }
    }
}

@Composable
private fun GuardianCallScreen(onClose: () -> Unit, isPremium: Boolean, onGoPremium: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    var isScreenSharing by remember { mutableStateOf(false) }

    val projectionManager = remember { context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager }
    val screenCaptureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            isScreenSharing = true
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // 1. Remote Parent Placeholder (Full Screen)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🏠", fontSize = 120.sp)
            Text("Guardian is here", color = Color.White, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            StatusPill("Standard Link")
            if (!isPremium) {
                Text(
                    "Switch to Priority Direct Link", 
                    color = Color.White.copy(alpha = 0.6f), 
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onGoPremium() }.padding(top = 12.dp)
                )
            }
        }

        // 2. Local Preview (Self-View)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(120.dp, 180.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            if (isScreenSharing) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.ScreenShare, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    Text("Sharing", color = Color.White, fontSize = 10.sp)
                }
            } else {
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                            controller = cameraController
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = {
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                )
            }
        }

        // 3. Controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // End Call
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.error, CircleShape)
            ) {
                Text("📞", fontSize = 24.sp, color = MaterialTheme.colorScheme.onError)
            }

            // Screen Share Toggle (LOCKED)
            IconButton(
                onClick = {
                    if (isPremium) {
                        if (isScreenSharing) isScreenSharing = false
                        else screenCaptureLauncher.launch(projectionManager.createScreenCaptureIntent())
                    } else onGoPremium()
                },
                modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Icon(Icons.Default.ScreenShare, contentDescription = "Share Screen", tint = MaterialTheme.colorScheme.onSurface)
                    if (!isPremium) Text("🔒", fontSize = 10.sp)
                }
            }

            // Flip Camera (LOCKED)
            IconButton(
                onClick = {
                    if (isPremium) {
                        cameraController.cameraSelector = 
                            if (cameraController.cameraSelector == androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA)
                                androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA 
                            else androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
                    } else onGoPremium()
                },
                modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), CircleShape)
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip Camera", tint = MaterialTheme.colorScheme.onSurface)
                    if (!isPremium) Text("🔒", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun FlowBlock(block: ScheduleBlock, speak: (String) -> Unit) {
    val color = when (block.type) {
        "work" -> MaterialTheme.colorScheme.primary
        "sensory" -> MaterialTheme.colorScheme.primary
        "break" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { speak("At ${block.time}, ${block.title}") }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            block.time,
            modifier = Modifier.width(70.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            block.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = if (block.type == "work") FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ClassRow(studyClass: StudyClass, speak: (String) -> Unit) {
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().clickable { speak("Class: ${studyClass.title}. Next block: ${studyClass.nextBlock}") }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(44.dp)
                    .clip(RoundedCornerShape(50))
                    .background(colorFromHex(studyClass.accentHex))
            )
            Column(Modifier.weight(1f)) {
                Text(studyClass.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(studyClass.priority, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(studyClass.nextBlock, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TaskRow(task: StudyTask, selected: Boolean, onSelect: () -> Unit, speak: (String) -> Unit) {
    OutlinedButton(
        onClick = { 
            onSelect()
            speak("Selected task: ${task.title}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .then(
                if (selected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
    ) {
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
            Text(task.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text("${task.course} - ${task.estimateMinutes} min - ${task.energy} energy", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(task.due, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun FocusScreen(
    task: StudyTask,
    session: FocusSession,
    onStartOrResume: () -> Unit,
    onPause: () -> Unit,
    onCompleteStep: () -> Unit,
    onCompleteSession: () -> Unit,
    onOpenSensory: () -> Unit,
    onReflect: () -> Unit,
    onGameReward: () -> Unit,
    speak: (String) -> Unit
) {
    if (session.status == SessionStatus.Transitioning) {
        TransitionScreen(
            session = session, 
            onSkip = onStartOrResume,
            onGameReward = onGameReward,
            speak = speak
        )
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().clickable { speak("Time remaining: ${formatRemainingTime(session.remainingSeconds)}") }
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val progress = if (session.durationSeconds == 0) 0f else session.remainingSeconds.toFloat() / session.durationSeconds.toFloat()
                Text("TimeAnchor", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .size(220.dp)
                            .semantics { contentDescription = "${formatRemainingTime(session.remainingSeconds)} remaining" },
                        strokeWidth = 12.dp
                    )
                    Text(
                        formatRemainingTime(session.remainingSeconds),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black
                    )
                }
                Text(
                    session.transitionWarning ?: "Transition support ready",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    when (session.status) {
                        SessionStatus.Active -> OutlinedButton(onClick = onPause) { Text("Pause") }
                        SessionStatus.Ready -> Button(onClick = onStartOrResume) { Text("Start") }
                        SessionStatus.Paused -> Button(onClick = onStartOrResume) { Text("Resume") }
                        SessionStatus.Complete -> Text(
                            "Session complete",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    OutlinedButton(
                        onClick = onCompleteSession,
                        enabled = session.status != SessionStatus.Complete
                    ) { Text("End") }
                }
            }
        }

        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("FocusFrame", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(currentStep(task, session), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                Text(task.title, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LinearProgressLabel(progress = sessionProgress(task, session))

                task.steps.forEachIndexed { index, step ->
                    StepRow(
                        index = index,
                        step = step,
                        complete = session.completedStepIndexes.contains(index),
                        current = index == session.activeStepIndex
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onCompleteStep,
                        enabled = session.status != SessionStatus.Complete && session.activeStepIndex < task.steps.size,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Complete current step")
                    }
                    OutlinedButton(onClick = onOpenSensory) { Text("Sensory") }
                    OutlinedButton(onClick = onReflect) { Text("Reflect") }
                }
            }
        }
    }
}

@Composable
private fun TransitionScreen(session: FocusSession, onSkip: () -> Unit, onGameReward: () -> Unit, speak: (String) -> Unit) {
    var showGame by remember { mutableStateOf(false) }
    var gameCompleted by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { speak("Transition Period. Take a deep breath.") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                if (showGame) "Executive Spark Game" else "Transition Period", 
                color = MaterialTheme.colorScheme.primary, 
                fontWeight = FontWeight.Bold
            )
            
            if (!showGame) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { session.transitionSeconds / 60f },
                        modifier = Modifier.size(160.dp),
                        strokeWidth = 8.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        formatRemainingTime(session.transitionSeconds),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                }
                
                Text(
                    "Take a deep breath. Gather your tools for ${session.taskTitle}.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (!gameCompleted) {
                    Button(
                        onClick = { showGame = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Play 10s Spark ⚡")
                    }
                } else {
                    StatusPill("Brain Sparked! +50 Tokens")
                }
                
                OutlinedButton(onClick = onSkip) {
                    Text("Ready now")
                }
            } else {
                SparkMiniGame(
                    onComplete = {
                        showGame = false
                        gameCompleted = true
                        onGameReward()
                    }
                )
            }
        }
    }
}

@Composable
fun SparkMiniGame(onComplete: () -> Unit) {
    var taps by remember { mutableStateOf(0) }
    val targetTaps = 10
    var sparkPosition by remember { mutableStateOf(0.5f to 0.5f) }
    
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Tap the spark $targetTaps times to wake up!", fontWeight = FontWeight.Bold)
        
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.neuro_nav_focus),
                contentDescription = "The Spark",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
                    .offset {
                        IntOffset(
                            x = ((sparkPosition.first - 0.5f) * 160.dp.toPx()).roundToInt(),
                            y = ((sparkPosition.second - 0.5f) * 160.dp.toPx()).roundToInt()
                        )
                    }
                    .clickable {
                        taps++
                        if (taps >= targetTaps) {
                            onComplete()
                        } else {
                            sparkPosition = (Math.random().toFloat()) to (Math.random().toFloat())
                        }
                    }
            )
        }
        
        LinearProgressIndicator(
            progress = { taps.toFloat() / targetTaps },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape)
        )
    }
}

@Composable
private fun LinearProgressLabel(progress: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Text("${(progress * 100).roundToInt()}% of steps complete", fontSize = 12.sp)
    }
}

@Composable
private fun StepRow(index: Int, step: String, complete: Boolean, current: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (current) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (complete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (index + 1).toString(),
                color = if (complete) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Black
            )
        }
        Text(
            text = step,
            color = if (complete) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (current) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun SensoryScreen(
    profile: NeuroProfile,
    onProfileChange: (NeuroProfile) -> Unit,
    controller: SensoryController
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionTitle("Visual profile")
        VisualProfiles.all.forEach { visualProfile ->
            ElevatedButton(
                onClick = { onProfileChange(profile.copy(sensoryProfile = visualProfile)) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (profile.sensoryProfile.id == visualProfile.id) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Text(visualProfile.label, fontWeight = FontWeight.Bold)
            }
        }

        SectionTitle("System-Wide Protection")
        
        SettingSwitch(
            title = "Do Not Disturb",
            checked = controller.isDndActive(),
            onCheckedChange = { controller.setDoNotDisturb(it) }
        )

        SettingSwitch(
            title = "Grayscale Mode",
            checked = controller.isGrayscaleEnabled(),
            onCheckedChange = { controller.setGrayscale(it) }
        )

        Card(shape = RoundedCornerShape(8.dp)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Screen Brightness", fontWeight = FontWeight.Bold)
                Slider(
                    value = 50f, // Simplified for prototype
                    onValueChange = { controller.setBrightness(it.toInt()) },
                    valueRange = 0f..100f
                )
            }
        }

        SectionTitle("Layout & Motion")
        
        SettingSwitch(
            title = "Speech Support (Read Aloud)",
            checked = profile.speechEnabled,
            onCheckedChange = { onProfileChange(profile.copy(speechEnabled = it)) }
        )

        SectionTitle("Auditory Sanctuary")
        listOf("None", "Lo-fi", "Rain", "White Noise").forEach { sound ->
            ElevatedButton(
                onClick = { onProfileChange(profile.copy(audioSupportId = sound.lowercase())) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (profile.audioSupportId == sound.lowercase()) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Text(sound, fontWeight = FontWeight.Bold)
            }
        }
        
        SettingSwitch(
            title = "Reduced movement",
            checked = profile.sensoryProfile.motion != "standard",
            onCheckedChange = { checked ->
                onProfileChange(
                    profile.copy(
                        sensoryProfile = profile.sensoryProfile.copy(motion = if (checked) "minimal" else "standard")
                    )
                )
            }
        )

        SettingSwitch(
            title = "Comfort spacing",
            checked = profile.sensoryProfile.readingComfort != "standard",
            onCheckedChange = { checked ->
                onProfileChange(
                    profile.copy(
                        sensoryProfile = profile.sensoryProfile.copy(readingComfort = if (checked) "wideSpacing" else "standard")
                    )
                )
            }
        )

        Card(shape = RoundedCornerShape(8.dp)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Notification intensity", fontWeight = FontWeight.Bold)
                Slider(
                    value = profile.sensoryProfile.notificationIntensity.toFloat(),
                    onValueChange = {
                        onProfileChange(
                            profile.copy(
                                sensoryProfile = profile.sensoryProfile.copy(notificationIntensity = it.roundToInt())
                            )
                        )
                    },
                    valueRange = 0f..4f,
                    steps = 3
                )
                Text(profile.sensoryProfile.notificationIntensity.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThemeMatrixScreen(profile: NeuroProfile, onProfileChange: (NeuroProfile) -> Unit) {
    val selectedTheme = FirstEditionThemes.find(profile.launcherThemeId)
    val ageOptions = listOf(
        "not-set" to "Skip",
        "6-9" to "6–9",
        "10-13" to "10–13",
        "14-17" to "14–17",
        "18-24" to "18–24",
        "25-plus" to "25+"
    )

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    "FIRST EDITION",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
                Text("Choose your flow", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text(
                    "Any theme can be used at any age and changed later.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            StatusPill(selectedTheme.name)
        }

        OutlinedCard(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Optional age guidance", fontWeight = FontWeight.Black)
                Text(
                    "Used only to suggest a starting theme. This selection stays on this device.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ageOptions.forEach { (id, label) ->
                        FilterChip(
                            selected = profile.ageGuidanceId == id,
                            onClick = {
                                val recommendation = FirstEditionThemes.recommend(id)
                                onProfileChange(profile.copy(ageGuidanceId = id, launcherThemeId = recommendation.id))
                            },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }

        SectionTitle("Theme matrix")
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FirstEditionThemes.all.forEach { theme ->
                val isPurchased = profile.purchasedThemeIds.contains(theme.id)
                ThemeMatrixCard(
                    theme = theme,
                    selected = theme.id == profile.launcherThemeId,
                    isPurchased = isPurchased,
                    onSelect = { 
                        if (isPurchased) {
                            onProfileChange(profile.copy(launcherThemeId = theme.id))
                        }
                    },
                    onBuy = {
                        if (profile.tokens >= theme.priceTokens) {
                            onProfileChange(profile.copy(
                                tokens = profile.tokens - theme.priceTokens,
                                purchasedThemeIds = profile.purchasedThemeIds + theme.id,
                                launcherThemeId = theme.id
                            ))
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colorFromHex(selectedTheme.backgroundHex)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("${selectedTheme.name} preview", fontWeight = FontWeight.Black, fontSize = 20.sp)
                Text(selectedTheme.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PaletteDot(selectedTheme.backgroundHex)
                    PaletteDot(selectedTheme.primaryHex)
                    PaletteDot(selectedTheme.accentHex)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    repeat(selectedTheme.gridColumns.coerceAtMost(5)) {
                        Box(
                            Modifier
                                .weight(1f)
                                .height(if (profile.launcherIconScale == "large") 58.dp else 46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(colorFromHex(selectedTheme.primaryHex).copy(alpha = 0.16f))
                        )
                    }
                }
            }
        }

        SectionTitle("Quick refinements")
        Text("These controls override the selected theme without changing its identity.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = profile.launcherIconScale == "standard",
                onClick = { onProfileChange(profile.copy(launcherIconScale = "standard")) },
                label = { Text("Standard icons") }
            )
            FilterChip(
                selected = profile.launcherIconScale == "large",
                onClick = { onProfileChange(profile.copy(launcherIconScale = "large")) },
                label = { Text("Large icons") }
            )
        }
        SettingSwitch(
            title = "Reduce launcher motion",
            checked = profile.launcherReducedMotion,
            onCheckedChange = { onProfileChange(profile.copy(launcherReducedMotion = it)) }
        )
        Text(
            "Contrast and reading comfort remain available in SensoryShield.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun ThemeMatrixCard(
    theme: LauncherTheme,
    selected: Boolean,
    isPurchased: Boolean,
    onSelect: () -> Unit,
    onBuy: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .widthIn(min = 160.dp)
            .clickable(onClick = if (isPurchased) onSelect else onBuy),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isPurchased) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorFromHex(theme.backgroundHex))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.fillMaxWidth().height(18.dp).clip(RoundedCornerShape(8.dp)).background(Color.White.copy(alpha = 0.86f)))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(theme.gridColumns.coerceAtMost(5)) {
                            val iconEmoji = when(theme.iconStyle) {
                                "nature" -> "🌿"
                                "space" -> "🪐"
                                "playful" -> "🎈"
                                "minimal" -> "▫️"
                                else -> ""
                            }
                            Box(
                                Modifier.weight(1f).height(30.dp).clip(RoundedCornerShape(8.dp)).background(colorFromHex(theme.primaryHex).copy(alpha = 0.22f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (iconEmoji.isNotEmpty()) {
                                    Text(iconEmoji, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
                
                if (!isPurchased) {
                    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)), contentAlignment = Alignment.Center) {
                        Text("🔒", fontSize = 24.sp)
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(theme.name, fontWeight = FontWeight.Black, modifier = Modifier.weight(1f))
                if (selected) {
                    Text("✓", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
                } else if (!isPurchased) {
                    Text("${theme.priceTokens} ✧", color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            Text("Suggested: ${theme.suggestedAges}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                PaletteDot(theme.backgroundHex)
                PaletteDot(theme.primaryHex)
                PaletteDot(theme.accentHex)
            }
            Text(theme.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, maxLines = 3)
            
            if (!isPurchased) {
                Button(
                    onClick = onBuy,
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Unlock", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PaletteDot(hex: String) {
    Box(
        Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(colorFromHex(hex))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(profile: NeuroProfile, onProfileChange: (NeuroProfile) -> Unit) {
    val options = listOf(
        "task-initiation" to "Task initiation",
        "time-awareness" to "Time awareness",
        "reading-support" to "Reading support",
        "transition-support" to "Transition support"
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("App Mode")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppThemeMode.entries.forEach { mode ->
                // Adventure Mode is now unlocked for a 'First Trip' for everyone
                FilterChip(
                    selected = profile.themeMode == mode,
                    onClick = { 
                        onProfileChange(profile.copy(themeMode = mode))
                    },
                    label = { 
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(mode.name)
                            if (mode == AppThemeMode.Kids && !profile.isPremium) Text("✨", fontSize = 10.sp)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        SectionTitle("Focus needs")
        options.forEach { (value, label) ->
            FilterChip(
                selected = profile.focusNeeds.contains(value),
                onClick = {
                    val next = if (profile.focusNeeds.contains(value)) {
                        profile.focusNeeds - value
                    } else {
                        profile.focusNeeds + value
                    }
                    onProfileChange(profile.copy(focusNeeds = next))
                },
                label = { Text(label) },
                modifier = Modifier.height(52.dp)
            )
        }

        SectionTitle("Time support")
        listOf("light", "guided", "high").forEach { level ->
            ElevatedButton(
                onClick = { onProfileChange(profile.copy(timeSupportLevel = level)) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = if (profile.timeSupportLevel == level) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Text(level, fontWeight = FontWeight.Bold)
            }
        }

        SectionTitle("Focus settings")
        SettingSwitch(
            title = "Filter distracting apps in Focus",
            checked = profile.filterAppsInFocus,
            onCheckedChange = { onProfileChange(profile.copy(filterAppsInFocus = it)) }
        )

        SettingSwitch(
            title = "Enable Text-to-Speech (Phonics)",
            checked = profile.speechEnabled,
            onCheckedChange = { onProfileChange(profile.copy(speechEnabled = it)) }
        )
        
        Text(
            text = "Note: Phonics requires a TTS engine (like Google Speech) to be installed on your device.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        OutlinedCard(shape = RoundedCornerShape(8.dp)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("CalmAI Companion", fontWeight = FontWeight.Black)
                Text(
                    "The personal neuro-assistant is currently being calibrated for your profile. Stay tuned for V1.1!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ReflectionScreen(session: FocusSession, onSave: (Reflection) -> Unit) {
    var easeScore by remember(session.id) { mutableFloatStateOf(session.reflection?.easeScore?.toFloat() ?: 3f) }
    var note by remember(session.id) { mutableStateOf(session.reflection?.note ?: "") }
    val checklist = userTestingChecklist()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Session reflection", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                Text("Ease score: ${easeScore.roundToInt()}", fontWeight = FontWeight.Bold)
                Slider(
                    value = easeScore,
                    onValueChange = { easeScore = it },
                    valueRange = 1f..5f,
                    steps = 3
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("What helped or got in the way?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Button(
                    onClick = {
                        onSave(
                            Reflection(
                                easeScore = easeScore.roundToInt(),
                                note = note
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save reflection", fontWeight = FontWeight.Bold)
                }
            }
        }

        SectionTitle("User-test checks")
        checklist.forEach {
            OutlinedCard(shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                Text(it, modifier = Modifier.padding(14.dp))
            }
        }
    }
}

@Composable
private fun SettingSwitch(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
private fun AboutScreen() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
        SectionTitle("The NeuroOS Mission")
        
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("🦖 From the Founder", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "“NeuroOS was born from lived experience. As a neurodivergent individual raising children and supporting family members on the ASD spectrum, I have spent years witnessing the 'digital friction' our community faces.\n\nCurrent technology is often a source of overwhelm rather than support. I wanted to create something that runs on the devices we have with us every second of the day—our mobile phones—to turn them from sources of chaos into tools for clarity. My dream is for NeuroOS to start here, but eventually grow into a full computer operating system that redefines how the neurodivergent world interacts with technology.”",
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp
                )
            }
        }

        SectionTitle("Product Pillars")
        
        val pillars = listOf(
            "🛡️ SensoryShield™" to "A one-tap 'Emergency Sanctuary' that instantly activates grayscale, therapeutic brightness, and strict 'Dopamine-Free' filtering.",
            "🔍 Guardian AI™" to "Predictive analytics that identifies 'Dopamine Windows' and warns of 'Energy Dips' to preempt meltdowns.",
            "🔋 Energy-Aware Planner" to "The first scheduling tool that tracks 'Mental Battery Cost' instead of just hours.",
            "🏝️ Kids Adventure Mode" to "An immersive, child-safe environment locked behind a 'Parent Gate' that gamifies daily hygiene and focus goals.",
            "📞 Guardian Link™" to "Real-time video and screen-sharing support, allowing parents to 'Body Double' or assist from anywhere."
        )

        pillars.forEach { (title, desc) ->
            OutlinedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        SectionTitle("Investment Highlights")
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                BulletPoint("Massive Market", "1 in 36 children diagnosed with ASD; 10% with ADHD.")
                BulletPoint("Privacy-First", "Safe-haven brand with local-only and peer-to-peer data.")
                BulletPoint("Scalability", "Built on Kotlin/Compose for Cross-Platform deployment.")
                BulletPoint("Freemium Model", "$1.99/mo or $15.00 Lifetime Unlock options.")
            }
        }
        
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun BulletPoint(label: String, desc: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("⚡", color = MaterialTheme.colorScheme.primary)
        Column {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(desc, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 4.dp)
    )
}

private fun colorSchemeFor(profile: NeuroProfile, darkTheme: Boolean): ColorScheme {
    if (profile.themeMode == AppThemeMode.Kids) {
        return darkColorScheme(
            primary = Color(0xFF00E5FF),
            onPrimary = Color.Black,
            secondary = Color(0xFFA29BFE),
            onSecondary = Color.Black,
            tertiary = Color(0xFFFFD54F),
            onTertiary = Color.Black,
            background = Color(0xFF0A192F),
            surface = Color(0xFF1E293B),
            onSurface = Color.White,
            surfaceVariant = Color(0xFF334155),
            onSurfaceVariant = Color(0xFFCBD5E1), // Brightened for readability
            error = Color(0xFFFF5252)
        )
    }

    return when (profile.sensoryProfile.id) {
        VisualProfiles.highContrast.id -> if (darkTheme) {
            darkColorScheme(
                primary = Color(0xFF67F2FF),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF003B46),
                secondary = Color(0xFFC9B8FF),
                secondaryContainer = Color(0xFF301B54),
                background = Color.Black,
                surface = Color(0xFF080A10),
                onSurface = Color.White,
                onSurfaceVariant = Color(0xFFCBD5E1), // Brightened
                outline = Color.White,
                outlineVariant = Color(0xFF68708A)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF005662),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFD8F8FF),
                secondary = Color(0xFF4D3396),
                secondaryContainer = Color(0xFFE9E0FF),
                background = Color.White,
                surface = Color.White,
                onSurface = Color.Black,
                onSurfaceVariant = Color.Black,
                outline = Color.Black,
                outlineVariant = Color.DarkGray
            )
        }

        VisualProfiles.readingComfort.id -> if (darkTheme) {
            darkColorScheme(
                primary = Color(0xFF72DCEB),
                onPrimary = Color(0xFF001014),
                primaryContainer = Color(0xFF12313D),
                secondary = Color(0xFFB6A9EA),
                secondaryContainer = Color(0xFF29213D),
                background = Color(0xFF0A0D15),
                surface = Color(0xFF101522),
                onSurface = Color(0xFFF1F3F8),
                onSurfaceVariant = Color(0xFFD1D9EA), // Brightened
                outlineVariant = Color(0xFF30394F)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF006874),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFBFAF5),
                secondary = Color(0xFF5F4E92),
                secondaryContainer = Color(0xFFF4F3ED),
                background = Color(0xFFF4F3ED),
                surface = Color(0xFFFBFAF5),
                onSurface = Color(0xFF1C1B1F),
                onSurfaceVariant = Color(0xFF49454F),
                outline = Color(0xFF79747E)
            )
        }

        else -> if (darkTheme) afterglowColorScheme() else daylightColorScheme()
    }
}

private fun colorFromHex(hex: String): Color = Color(android.graphics.Color.parseColor(hex))
