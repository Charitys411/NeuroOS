package com.neuroos.app

import android.content.Context

class NeuroStorage(context: Context) {
    private val prefs = context.getSharedPreferences("neuroos_settings", Context.MODE_PRIVATE)

    fun loadProfile(): NeuroProfile {
        val visualProfile = VisualProfiles.find(prefs.getString("visualProfileId", VisualProfiles.calm.id) ?: VisualProfiles.calm.id)
        val needs = prefs.getStringSet("focusNeeds", setOf("task-initiation", "time-awareness"))
            ?: setOf("task-initiation", "time-awareness")
        val timeSupport = prefs.getString("timeSupportLevel", "guided") ?: "guided"
        val filterApps = prefs.getBoolean("filterAppsInFocus", true)
        val themeModeStr = prefs.getString("themeMode", AppThemeMode.Default.name) ?: AppThemeMode.Default.name
        val themeMode = try { AppThemeMode.valueOf(themeModeStr) } catch(e: Exception) { AppThemeMode.Default }

        return NeuroProfile(
            focusNeeds = needs,
            sensoryProfile = visualProfile,
            timeSupportLevel = timeSupport,
            filterAppsInFocus = filterApps,
            tokens = prefs.getInt("tokens", 0),
            robuxBalance = prefs.getInt("robuxBalance", 0),
            stickers = prefs.getStringSet("stickers", emptySet()) ?: emptySet(),
            themeMode = themeMode,
            isPremium = prefs.getBoolean("isPremium", false),
            usageLogs = loadUsageLogs(),
            memoryNeeds = loadMemoryNeeds(),
            launcherThemeId = prefs.getString("launcherThemeId", FirstEditionThemes.campus.id)
                ?: FirstEditionThemes.campus.id,
            purchasedThemeIds = prefs.getStringSet("purchasedThemeIds", FirstEditionThemes.all.filter { it.priceTokens == 0 }.map { it.id }.toSet())
                ?: setOf("cyber", "kids", "sprout", "orbit", "momentum", "campus", "evergreen"),
            ageGuidanceId = prefs.getString("ageGuidanceId", "not-set") ?: "not-set",
            launcherIconScale = prefs.getString("launcherIconScale", "standard") ?: "standard",
            launcherReducedMotion = prefs.getBoolean("launcherReducedMotion", true),
            speechEnabled = prefs.getBoolean("speechEnabled", true)
        )
    }

    fun saveProfile(profile: NeuroProfile) {
        prefs.edit()
            .putString("visualProfileId", profile.sensoryProfile.id)
            .putStringSet("focusNeeds", profile.focusNeeds)
            .putString("timeSupportLevel", profile.timeSupportLevel)
            .putBoolean("filterAppsInFocus", profile.filterAppsInFocus)
            .putInt("tokens", profile.tokens)
            .putInt("robuxBalance", profile.robuxBalance)
            .putStringSet("stickers", profile.stickers)
            .putString("themeMode", profile.themeMode.name)
            .putBoolean("isPremium", profile.isPremium)
            .putString("launcherThemeId", profile.launcherThemeId)
            .putStringSet("purchasedThemeIds", profile.purchasedThemeIds)
            .putString("ageGuidanceId", profile.ageGuidanceId)
            .putString("launcherIconScale", profile.launcherIconScale)
            .putBoolean("launcherReducedMotion", profile.launcherReducedMotion)
            .putBoolean("speechEnabled", profile.speechEnabled)
            .apply()
        
        saveUsageLogs(profile.usageLogs)
        saveMemoryNeeds(profile.memoryNeeds)
    }

    private fun loadUsageLogs(): List<UsageLog> {
        val raw = prefs.getString("usageLogs", "") ?: ""
        if (raw.isEmpty()) return emptyList()
        return raw.split(";").mapNotNull {
            val parts = it.split(",")
            if (parts.size == 4) {
                UsageLog(parts[0], parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
            } else null
        }
    }

    private fun saveUsageLogs(logs: List<UsageLog>) {
        val raw = logs.joinToString(";") { "${it.packageName},${it.dayOfWeek},${it.hourOfDay},${it.count}" }
        prefs.edit().putString("usageLogs", raw).apply()
    }

    private fun loadMemoryNeeds(): List<MemoryNeed> {
        val raw = prefs.getString("memoryNeeds", "") ?: ""
        if (raw.isEmpty()) return listOf(
            MemoryNeed("mn1", "Budget Check", "💰", "Remind me to look at my money."),
            MemoryNeed("mn2", "Calendar Sync", "📅", "Remind me of upcoming appointments."),
            MemoryNeed("mn3", "Medication/Routine", "💊", "Help me remember daily health tasks."),
            MemoryNeed("mn4", "Social Connection", "👋", "Remind me to check in with a friend.")
        )
        return raw.split(";").mapNotNull {
            val parts = it.split("|")
            if (parts.size == 5) {
                MemoryNeed(parts[0], parts[1], parts[2], parts[3], parts[4].toBoolean())
            } else null
        }
    }

    private fun saveMemoryNeeds(needs: List<MemoryNeed>) {
        val raw = needs.joinToString(";") { "${it.id}|${it.title}|${it.icon}|${it.description}|${it.enabled}" }
        prefs.edit().putString("memoryNeeds", raw).apply()
    }

    fun loadEnergyLogs(): List<EnergyEntry> {
        val raw = prefs.getString("energyLogs", "") ?: ""
        if (raw.isEmpty()) return emptyList()
        return raw.split(";").mapNotNull {
            val parts = it.split(",")
            if (parts.size == 2) {
                EnergyEntry(parts[0].toInt(), parts[1].toLong())
            } else null
        }
    }

    fun saveEnergyLogs(logs: List<EnergyEntry>) {
        val raw = logs.joinToString(";") { "${it.level},${it.timestamp}" }
        prefs.edit().putString("energyLogs", raw).apply()
    }
}
