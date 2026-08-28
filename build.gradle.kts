plugins {
    id("com.android.application") version "9.3.2" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.21" apply false
}

allprojects {
    // Redirect build outputs outside of OneDrive to avoid file locking issues
    layout.buildDirectory.set(file("C:/Users/grice/gradle-builds/NeuroOS/${project.name}"))
}
