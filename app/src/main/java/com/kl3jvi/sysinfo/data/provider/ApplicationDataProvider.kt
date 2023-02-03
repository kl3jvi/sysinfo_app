package com.kl3jvi.sysinfo.data.provider

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

class ApplicationDataProvider(
    private val packageManager: PackageManager
) {
    @Suppress("DEPRECATION")
    fun getInstalledApplications(withSystemApps: Boolean): List<ApplicationInfo> {
        val applications = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledApplications(
                PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        } else {
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        }
        return if (withSystemApps) {
            applications
        } else {
            applications.filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
        }
    }
}
