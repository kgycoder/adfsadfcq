package com.ioslauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable?,
    val isSystemApp: Boolean = false
)

object AppRepository {
    fun getInstalledApps(context: Context): List<AppInfo> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return pm.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != context.packageName }
            .map { ri ->
                AppInfo(
                    name = ri.loadLabel(pm).toString(),
                    packageName = ri.activityInfo.packageName,
                    icon = try { ri.loadIcon(pm) } catch (e: Exception) { null },
                    isSystemApp = (ri.activityInfo.applicationInfo.flags and
                            android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
                )
            }
            .sortedBy { it.name.lowercase() }
    }

    fun getDockApps(context: Context): List<AppInfo> {
        val allApps = getInstalledApps(context)
        val preferred = listOf(
            "com.android.dialer",
            "com.samsung.android.messaging",
            "com.android.chrome",
            "com.sec.android.app.camera"
        )
        val dockApps = mutableListOf<AppInfo>()
        preferred.forEach { pkg ->
            allApps.find { it.packageName == pkg }?.let { dockApps.add(it) }
        }
        if (dockApps.size < 4) {
            allApps.filter { app -> dockApps.none { it.packageName == app.packageName } }
                .take(4 - dockApps.size)
                .let { dockApps.addAll(it) }
        }
        return dockApps.take(4)
    }
}
