package com.neuroos.app

data class VisualProfile(
    val id: String,
    val label: String,
    val motion: String,
    val contrast: String,
    val readingComfort: String,
    val notificationIntensity: Int
)

enum class AppThemeMode {
    Default,
    Teens,
    Work,
    Kids,
    Adult
}

object NeuroProducts {
    const val PREMIUM_MONTHLY = "neuro_os_premium_monthly"
    const val PREMIUM_LIFETIME = "neuro_os_premium_lifetime"
}

data class RewardGoal(
    val id: String,
    val title: String,
    val tokenCost: Int,
    val type: String // "robux", "sticker", "game", "parent-choice"
)

data class GuardianAiInsight(
    val title: String,
    val summary: String,
    val suggestion: String,
    val emotion: String // "happy", "caution", "alert"
)

data class Sticker(
    val id: String,
    val name: String,
    val emoji: String,
    val rarity: String // "Common", "Rare", "Epic"
)

data class FinancialEntry(
    val id: String,
    val title: String,
    val amount: Double,
    val dueDate: String?, // For bills
    val type: String // "income", "bill"
)

data class GroundingStep(
    val prompt: String,
    val instruction: String
)

data class InertiaGame(
    val id: String,
    val name: String,
    val emoji: String,
    val goal: String
)

object AdultToolkit {
    val inertiaGames = listOf(
        InertiaGame("spark", "The 10-Second Spark", "⚡", "Tap the spark as fast as you can!"),
        InertiaGame("match", "Pattern Pop", "🧩", "Match the colors to wake up your brain.")
    )
    val grounding54321 = listOf(
        GroundingStep("5 things you see", "Look around and name them silently."),
        GroundingStep("4 things you feel", "Focus on physical touch (e.g., seat, cloth)."),
        GroundingStep("3 things you hear", "Listen for background hums or nature."),
        GroundingStep("2 things you smell", "Identify subtle scents in the air."),
        GroundingStep("1 thing you taste", "Focus on the current taste in your mouth.")
    )
}

data class UsageLog(
    val packageName: String,
    val dayOfWeek: Int, // 1-7
    val hourOfDay: Int, // 0-23
    val count: Int = 1
)

data class MemoryNeed(
    val id: String,
    val title: String,
    val icon: String,
    val description: String,
    val enabled: Boolean = false
)

data class NeuroProfile(
    val studentSegment: String = "adaptive",
    val focusNeeds: Set<String> = setOf("task-initiation", "time-awareness", "reading-support"),
    val sensoryProfile: VisualProfile = VisualProfiles.calm,
    val readingSupport: String = "comfortable-spacing",
    val timeSupportLevel: String = "guided",
    val calmAiEnabled: Boolean = true,
    val filterAppsInFocus: Boolean = true,
    val tokens: Int = 450,
    val robuxBalance: Int = 40,
    val stickers: Set<String> = setOf("s1", "s2", "s5"),
    val themeMode: AppThemeMode = AppThemeMode.Default,
    val launcherThemeId: String = "cyber",
    val purchasedThemeIds: Set<String> = setOf("cyber", "kids", "sprout", "orbit", "momentum", "campus", "evergreen"),
    val speechEnabled: Boolean = true,
    val literacySupportActive: Boolean = true,
    val audioSupportId: String = "lo-fi",
    val isPremium: Boolean = true,
    val usageLogs: List<UsageLog> = emptyList(),
    val memoryNeeds: List<MemoryNeed> = listOf(
        MemoryNeed("mn1", "Budget Check", "💰", "Remind me to look at my money."),
        MemoryNeed("mn2", "Calendar Sync", "📅", "Remind me of upcoming appointments."),
        MemoryNeed("mn3", "Medication/Routine", "💊", "Help me remember daily health tasks."),
        MemoryNeed("mn4", "Social Connection", "👋", "Remind me to check in with a friend.")
    ),
    val rewardGoals: List<RewardGoal> = listOf(
        RewardGoal("rg1", "10 Robux", 100, "robux"),
        RewardGoal("rg2", "Dinosaur Sticker", 50, "sticker"),
        RewardGoal("rg3", "Unlock 'Focus Flow' Mini-game", 200, "game")
    ),
    val ageGuidanceId: String = "not-set",
    val launcherIconScale: String = "standard",
    val launcherReducedMotion: Boolean = true
)

data class LauncherTheme(
    val id: String,
    val name: String,
    val suggestedAges: String,
    val description: String,
    val backgroundHex: String,
    val primaryHex: String,
    val accentHex: String,
    val gridColumns: Int,
    val priceTokens: Int = 0,
    val iconStyle: String = "circuit" // "circuit", "nature", "space", "minimal"
)

object FirstEditionThemes {
    val cyber = LauncherTheme("cyber", "Cyber Sci-Fi", "All Ages", "High-fidelity electric purple and gold interface.", "#000000", "#8E44AD", "#F1C40F", 5)
    val kids = LauncherTheme("kids", "Adventure Mode", "6-12", "Playful, bright, and easy-to-navigate interface.", "#0A192F", "#00E5FF", "#FFD54F", 3, iconStyle = "playful")
    val sprout = LauncherTheme("sprout", "Sprout", "6–9", "Simple Now/Next focus with maximum icon clarity.", "#0A192F", "#00E5FF", "#FFD54F", 4, iconStyle = "playful")
    val orbit = LauncherTheme("orbit", "Orbit", "10–13", "Category-first apps with school and routine widgets.", "#080C18", "#00CFF5", "#A29BFE", 4, iconStyle = "space")
    val momentum = LauncherTheme("momentum", "Momentum", "14–17", "Schedule-centered layout with quieter notifications.", "#070A12", "#00E5FF", "#A855F7", 5)
    val campus = LauncherTheme("campus", "Campus", "18–24", "Full productivity stack with FocusFrame integration.", "#070A12", "#00E5FF", "#A29BFE", 5)
    val evergreen = LauncherTheme("evergreen", "Evergreen", "25+", "Low-motion, high-readability support for balanced routines.", "#0A0D15", "#72DCEB", "#B6A9EA", 5, iconStyle = "nature")

    // Creative/Premium Themes
    val zenGarden = LauncherTheme("zen", "Zen Bonsai", "All Ages", "Tranquil purple canopy for deep meditation.", "#1A0A2A", "#A29BFE", "#FFD54F", 5, 250, iconStyle = "nature")
    val jungle = LauncherTheme("jungle", "Midnight Jungle", "All Ages", "Lush green depth for natural focus.", "#051A0A", "#4CAF50", "#00E5FF", 5, 250, iconStyle = "nature")
    val stellar = LauncherTheme("stellar", "Stellar Void", "All Ages", "Deep space harmonics for infinite thinking.", "#050811", "#A855F7", "#00E5FF", 5, 300, iconStyle = "space")
    val lavender = LauncherTheme("lavender", "Lavender Field", "All Ages", "Soothing violet rows for calming routines.", "#1C142A", "#D1D1FF", "#A29BFE", 5, 200, iconStyle = "nature")
    val ocean = LauncherTheme("ocean", "Ocean Serenity", "All Ages", "Crisp sea spray for energetic mornings.", "#E1F5FE", "#007F9E", "#FFD54F", 5, 200, iconStyle = "nature")
    val monarch = LauncherTheme("monarch", "Vibrant Monarch", "All Ages", "Intricate patterns for complex productivity.", "#1A1A1A", "#FF9100", "#00E5FF", 5, 300, iconStyle = "nature")
    val crimson = LauncherTheme("crimson", "Crimson Petals", "All Ages", "Bold floral depth for passionate work.", "#1A0510", "#FF647C", "#A29BFE", 5, 250, iconStyle = "nature")
    val lunar = LauncherTheme("lunar", "Lunar Dream", "All Ages", "Whimsical moonlit vistas for creativity.", "#0A0A1A", "#A29BFE", "#FFD54F", 5, 350, iconStyle = "space")
    val eagle = LauncherTheme("eagle", "Golden Majesty", "All Ages", "High-contrast dignity for focused leadership.", "#0B0E1A", "#FFD54F", "#F4F7FF", 5, 200, iconStyle = "minimal")
    val flamingo = LauncherTheme("flamingo", "Flamingo Sunset", "All Ages", "Vibrant tropical balance for evening calm.", "#2A0A1A", "#FF647C", "#00E5FF", 5, 250, iconStyle = "nature")
    val scarecrow = LauncherTheme("scarecrow", "Autumn Guardian", "All Ages", "Rustic harvest tones for steady productivity.", "#1A1005", "#9A6800", "#FFD54F", 5, 200, iconStyle = "nature")

    val all = listOf(cyber, kids, sprout, orbit, momentum, campus, evergreen, zenGarden, jungle, stellar, lavender, ocean, monarch, crimson, lunar, eagle, flamingo, scarecrow)

    fun find(id: String): LauncherTheme = all.firstOrNull { it.id == id } ?: cyber


    fun recommend(ageGuidanceId: String): LauncherTheme = when (ageGuidanceId) {
        "6-9" -> kids
        "10-13" -> orbit
        "14-17" -> momentum
        "18-24" -> campus
        "25-plus" -> evergreen
        else -> cyber
    }
}

data class ScheduleBlock(
    val id: String,
    val title: String,
    val time: String,
    val type: String // "work", "break", "sensory", "life"
)

data class PlannerItem(
    val id: String,
    val title: String,
    val day: String,
    val energyCost: Int, // 1-5
    val completed: Boolean = false
)

data class RoutineStep(
    val id: String,
    val title: String,
    val icon: String,
    val durationSeconds: Int = 120,
    val completed: Boolean = false
)

data class RoutineBoard(
    val id: String,
    val title: String,
    val steps: List<RoutineStep>
)

data class CommunicationCard(
    val id: String,
    val label: String,
    val icon: String,
    val phonics: String,
    val soundType: String // "vocal", "object", "action"
)

object CommunicationRepository {
    val needs = listOf(
        CommunicationCard("c1", "Water", "💧", "Wuh-ter", "object"),
        CommunicationCard("c2", "Food", "🍎", "Foo-d", "object"),
        CommunicationCard("c3", "Bathroom", "🚽", "Pot-tee", "action"),
        CommunicationCard("c4", "Help", "🙋", "Heh-lp", "vocal"),
        CommunicationCard("c5", "Break", "🧘", "Bray-k", "action"),
        CommunicationCard("c6", "Sleep", "😴", "Slee-p", "action")
    )
}

object RoutineRepository {
    val pottyTraining = RoutineBoard("rb1", "Potty Time! 🚽", listOf(
        RoutineStep("p1", "Go to Bathroom", "🚪"),
        RoutineStep("p2", "Sit & Wait", "🧘"),
        RoutineStep("p3", "Wipe", "🧻"),
        RoutineStep("p4", "Flush", "🚽"),
        RoutineStep("p5", "Wash Hands", "🧼")
    ))

    val morningSpark = RoutineBoard("rb2", "Morning Shine ☀️", listOf(
        RoutineStep("m1", "Brush Teeth", "🪥"),
        RoutineStep("m2", "Wash Face", "💦"),
        RoutineStep("m3", "Brush Hair", "🪮"),
        RoutineStep("m4", "Get Dressed", "👕")
    ))
}

data class AppInfo(
    val label: CharSequence,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable,
    val category: Int = -1
)

fun isDistracting(app: AppInfo): Boolean {
    // Android categories: 0=Game, 1=Audio, 2=Video, 3=Image, 4=Social, 5=News, 6=Maps, 7=Productivity
    val distractingCategories = setOf(0, 4, 5) // Games, Social, News
    return app.category in distractingCategories
}

data class StudyClass(
    val id: String,
    val title: String,
    val accentHex: String,
    val nextBlock: String,
    val priority: String
)

data class StudyTask(
    val id: String,
    val classId: String,
    val course: String,
    val title: String,
    val due: String,
    val estimateMinutes: Int,
    val energy: String,
    val steps: List<String>
)

enum class SessionStatus {
    Ready,
    Transitioning,
    Active,
    Paused,
    Complete
}

data class Reflection(
    val easeScore: Int = 3,
    val sensoryFit: String = "comfortable",
    val nextSessionNeed: String = "same-setup",
    val note: String = "",
    val createdAtMillis: Long = System.currentTimeMillis()
)

data class EnergyEntry(
    val level: Int, // 1-5
    val timestamp: Long = System.currentTimeMillis()
)

data class NeuroStats(
    val totalFocusMinutes: Int = 0,
    val averageEaseScore: Float = 0f,
    val sessionsCompleted: Int = 0,
    val energyTrend: List<EnergyEntry> = emptyList()
)

data class FocusSession(
    val id: String,
    val taskId: String,
    val taskTitle: String,
    val course: String,
    val status: SessionStatus = SessionStatus.Ready,
    val durationSeconds: Int,
    val remainingSeconds: Int,
    val transitionSeconds: Int = 60, // Default 1 minute transition
    val totalSteps: Int,
    val activeStepIndex: Int = 0,
    val completedStepIndexes: Set<Int> = emptySet(),
    val startedAtMillis: Long? = null,
    val endedAtMillis: Long? = null,
    val transitionWarning: String? = null,
    val reflection: Reflection? = null
)

sealed class SessionAction {
    data class StartTransition(val nowMillis: Long = System.currentTimeMillis()) : SessionAction()
    data class Start(val nowMillis: Long = System.currentTimeMillis()) : SessionAction()
    data object Pause : SessionAction()
    data object Resume : SessionAction()
    data class Tick(val seconds: Int = 1, val nowMillis: Long = System.currentTimeMillis()) : SessionAction()
    data object CompleteStep : SessionAction()
    data class CompleteSession(val nowMillis: Long = System.currentTimeMillis()) : SessionAction()
    data class Reflect(val reflection: Reflection) : SessionAction()
}

object VisualProfiles {
    val calm = VisualProfile(
        id = "calm",
        label = "Calm Default",
        motion = "reduced",
        contrast = "balanced",
        readingComfort = "standard",
        notificationIntensity = 2
    )

    val highContrast = VisualProfile(
        id = "highContrast",
        label = "High Contrast",
        motion = "minimal",
        contrast = "high",
        readingComfort = "standard",
        notificationIntensity = 1
    )

    val readingComfort = VisualProfile(
        id = "readingComfort",
        label = "Reading Comfort",
        motion = "reduced",
        contrast = "soft",
        readingComfort = "wideSpacing",
        notificationIntensity = 1
    )

    val all = listOf(calm, highContrast, readingComfort)

    fun find(id: String): VisualProfile = all.firstOrNull { it.id == id } ?: calm
}

object NeuroRepository {
    val stickerCatalog = listOf(
        Sticker("s1", "Happy Dino", "🦖", "Common"),
        Sticker("s2", "Rocket Ship", "🚀", "Common"),
        Sticker("s3", "Gold Star", "⭐", "Rare"),
        Sticker("s4", "Unicorn", "🦄", "Epic"),
        Sticker("s5", "Robot", "🤖", "Rare")
    )

    val adultFinances = listOf(
        FinancialEntry("f1", "Monthly Income", 3200.0, null, "income"),
        FinancialEntry("f2", "Rent Payment", 1200.0, "Aug 15", "bill"),
        FinancialEntry("f3", "Electric Bill", 85.0, "Aug 12", "bill"),
        FinancialEntry("f4", "Grocery Budget", 400.0, null, "bill")
    )

    val dailySchedule = listOf(
        ScheduleBlock("1", "Morning Routine", "8:00 AM", "life"),
        ScheduleBlock("2", "Deep Focus: Study", "9:30 AM", "work"),
        ScheduleBlock("3", "Sensory Reset", "11:00 AM", "sensory"),
        ScheduleBlock("4", "Lunch & Social", "12:00 PM", "life"),
        ScheduleBlock("5", "Collaborative Work", "1:30 PM", "work")
    )

    val weeklyPlanner = listOf(
        PlannerItem("p1", "Complete Bio Lab Report", "Mon", 5),
        PlannerItem("p2", "Library Research", "Mon", 3),
        PlannerItem("p3", "Grocery Shopping", "Mon", 2),
        PlannerItem("p4", "Algebra Quiz Prep", "Tue", 4),
        PlannerItem("p5", "15m Sensory Break", "Tue", 1),
        PlannerItem("p6", "Psychology Reading", "Tue", 3),
        PlannerItem("p7", "English Essay Draft", "Wed", 4),
        PlannerItem("p8", "Laundry / Routine", "Wed", 2),
        PlannerItem("p9", "History Presentation", "Thu", 5),
        PlannerItem("p10", "DND Session / Reward", "Fri", 1)
    )

    val classes = listOf(
        StudyClass(
            id = "cog-210",
            title = "Cognitive Psychology",
            accentHex = "#A855F7", // Updated to Electric Purple Brand
            nextBlock = "Today 2:00 PM",
            priority = "Chapter 8 retrieval practice"
        ),
        StudyClass(
            id = "bio-144",
            title = "Biology Lab",
            accentHex = "#FFD54F",
            nextBlock = "Tomorrow 10:30 AM",
            priority = "Lab report outline"
        ),
        StudyClass(
            id = "eng-302",
            title = "Writing Seminar",
            accentHex = "#A7494F",
            nextBlock = "Fri 11:00 AM",
            priority = "Argument map revision"
        )
    )

    val tasks = listOf(
        StudyTask(
            id = "task-cog-retrieval",
            classId = "cog-210",
            course = "Cognitive Psychology",
            title = "Prepare for Chapter 8 quiz",
            due = "Tonight",
            estimateMinutes = 35,
            energy = "medium",
            steps = listOf(
                "Open Chapter 8 notes",
                "Mark three concepts that feel unclear",
                "Answer five retrieval questions",
                "Write one summary card",
                "Choose the next review time"
            )
        ),
        StudyTask(
            id = "task-bio-outline",
            classId = "bio-144",
            course = "Biology Lab",
            title = "Outline lab report discussion",
            due = "Tomorrow",
            estimateMinutes = 25,
            energy = "low",
            steps = listOf(
                "Open the lab rubric",
                "Write the claim in one sentence",
                "Add two evidence bullets",
                "List one question for the TA"
            )
        ),
        StudyTask(
            id = "task-eng-map",
            classId = "eng-302",
            course = "Writing Seminar",
            title = "Revise essay argument map",
            due = "Friday",
            estimateMinutes = 45,
            energy = "high",
            steps = listOf(
                "Open the current argument map",
                "Circle the thesis",
                "Check each paragraph against the thesis",
                "Move one weak support point",
                "Save the revision note"
            )
        )
    )
}

fun createFocusSession(task: StudyTask, durationMinutes: Int = 25, transitionMinutes: Int = 1): FocusSession {
    val durationSeconds = durationMinutes.coerceAtLeast(1) * 60
    return FocusSession(
        id = "session-${task.id}",
        taskId = task.id,
        taskTitle = task.title,
        course = task.course,
        durationSeconds = durationSeconds,
        remainingSeconds = durationSeconds,
        transitionSeconds = transitionMinutes * 60,
        totalSteps = task.steps.size
    )
}

fun currentStep(task: StudyTask, session: FocusSession): String =
    task.steps.getOrNull(session.activeStepIndex) ?: "Session complete"

fun sessionProgress(task: StudyTask, session: FocusSession): Float =
    if (task.steps.isEmpty()) 1f else session.completedStepIndexes.size.toFloat() / task.steps.size.toFloat()

fun transitionWarning(remainingSeconds: Int): String? =
    when {
        remainingSeconds <= 0 -> "Session complete"
        remainingSeconds <= 60 -> "One minute left"
        remainingSeconds <= 300 -> "Five minute transition"
        else -> null
    }

fun reduceSession(session: FocusSession, action: SessionAction): FocusSession =
    when (action) {
        is SessionAction.StartTransition -> if (session.status == SessionStatus.Ready) {
            session.copy(status = SessionStatus.Transitioning)
        } else {
            session
        }

        is SessionAction.Start -> if (session.status == SessionStatus.Ready || session.status == SessionStatus.Transitioning) {
            session.copy(
                status = SessionStatus.Active,
                startedAtMillis = action.nowMillis
            )
        } else {
            session
        }

        SessionAction.Pause -> if (session.status == SessionStatus.Active || session.status == SessionStatus.Transitioning) {
            session.copy(status = SessionStatus.Paused)
        } else {
            session
        }

        SessionAction.Resume -> if (session.status == SessionStatus.Paused) {
            session.copy(status = if (session.startedAtMillis == null) SessionStatus.Transitioning else SessionStatus.Active)
        } else {
            session
        }

        is SessionAction.Tick -> {
            when (session.status) {
                SessionStatus.Transitioning -> {
                    val elapsed = action.seconds.coerceAtLeast(0)
                    val remaining = (session.transitionSeconds - elapsed).coerceAtLeast(0)
                    if (remaining == 0) {
                        session.copy(
                            status = SessionStatus.Active,
                            transitionSeconds = 0,
                            startedAtMillis = action.nowMillis
                        )
                    } else {
                        session.copy(transitionSeconds = remaining)
                    }
                }
                SessionStatus.Active -> {
                    val elapsed = action.seconds.coerceAtLeast(0)
                    val remaining = (session.remainingSeconds - elapsed).coerceAtLeast(0)
                    session.copy(
                        remainingSeconds = remaining,
                        status = if (remaining == 0) SessionStatus.Complete else session.status,
                        transitionWarning = transitionWarning(remaining),
                        endedAtMillis = if (remaining == 0) action.nowMillis else session.endedAtMillis
                    )
                }
                else -> session
            }
        }

        SessionAction.CompleteStep -> if (session.activeStepIndex < session.totalSteps) {
            session.copy(
                completedStepIndexes = session.completedStepIndexes + session.activeStepIndex,
                activeStepIndex = session.activeStepIndex + 1
            )
        } else {
            session
        }

        is SessionAction.CompleteSession -> session.copy(
            status = SessionStatus.Complete,
            remainingSeconds = 0,
            transitionWarning = "Session complete",
            endedAtMillis = action.nowMillis
        )

        is SessionAction.Reflect -> session.copy(reflection = action.reflection)
    }

fun formatRemainingTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

fun userTestingChecklist(): List<String> = listOf(
    "Student can choose a profile without help",
    "Student can identify the next step within five seconds",
    "Student can adjust sensory settings during focus",
    "Student can explain remaining time and transition warning",
    "Student can complete reflection without guidance"
)
