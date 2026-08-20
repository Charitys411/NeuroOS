package com.neuroos.app

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

class SensoryController(private val context: Context) {

    /**
     * Brightness Control
     * Requires android.permission.WRITE_SETTINGS
     */
    fun setBrightness(level: Int) {
        if (!canWriteSettings()) {
            openWriteSettingsPermission()
            return
        }
        val value = (level.coerceIn(0, 100) / 100f * 255).toInt()
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            value
        )
    }

    fun canWriteSettings(): Boolean = Settings.System.canWrite(context)

    private fun openWriteSettingsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * Do Not Disturb (DND)
     * Requires android.permission.ACCESS_NOTIFICATION_POLICY
     */
    fun setDoNotDisturb(enabled: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            return
        }
        val filter = if (enabled) {
            NotificationManager.INTERRUPTION_FILTER_PRIORITY
        } else {
            NotificationManager.INTERRUPTION_FILTER_ALL
        }
        notificationManager.setInterruptionFilter(filter)
    }

    fun isDndActive(): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_ALL
    }

    /**
     * Grayscale (Accessibility Daltonizer)
     * Requires android.permission.WRITE_SECURE_SETTINGS
     * (Normally granted via ADB for third-party launchers)
     */
    fun setGrayscale(enabled: Boolean) {
        try {
            Settings.Secure.putInt(context.contentResolver, "accessibility_display_daltonizer", 0)
            Settings.Secure.putInt(
                context.contentResolver,
                "accessibility_display_daltonizer_enabled",
                if (enabled) 1 else 0
            )
        } catch (e: SecurityException) {
            // If permission not granted, open the system accessibility settings as a fallback
            openAccessibilitySettings()
        }
    }

    fun isGrayscaleEnabled(): Boolean {
        return Settings.Secure.getInt(
            context.contentResolver,
            "accessibility_display_daltonizer_enabled",
            0
        ) == 1
    }

    fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
