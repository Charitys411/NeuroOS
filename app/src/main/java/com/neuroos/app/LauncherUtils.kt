package com.neuroos.app

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.Build

fun getInstalledApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
    
    val resolveInfos = pm.queryIntentActivities(mainIntent, 0)
    return resolveInfos.map { resolveInfo ->
        val appInfo = resolveInfo.activityInfo.applicationInfo
        val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appInfo.category
        } else {
            -1
        }
        
        AppInfo(
            label = resolveInfo.loadLabel(pm),
            packageName = resolveInfo.activityInfo.packageName,
            icon = resolveInfo.loadIcon(pm),
            category = category
        )
    }.sortedBy { it.label.toString().lowercase() }
}

fun launchApp(context: Context, packageName: String) {
    val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) {
        context.startActivity(launchIntent)
    }
}
