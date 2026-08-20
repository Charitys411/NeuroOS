# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\grice\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# NeuroOS Production R8 Rules

# Preserve our core data models from being obfuscated or stripped
-keep class com.neuroos.app.NeuroProfile { *; }
-keep class com.neuroos.app.VisualProfile { *; }
-keep class com.neuroos.app.FocusSession { *; }
-keep class com.neuroos.app.EnergyEntry { *; }
-keep class com.neuroos.app.UsageLog { *; }

# Google Play Billing Library safety
-keep class com.android.billingclient.api.** { *; }

# CameraX safety
-keep class androidx.camera.core.** { *; }
-keep class androidx.camera.lifecycle.** { *; }

# Compose general stability
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.ui.platform.** { *; }
