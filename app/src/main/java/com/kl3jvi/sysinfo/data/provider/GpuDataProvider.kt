package com.kl3jvi.sysinfo.data.provider

import android.app.ActivityManager
import org.koin.core.component.KoinComponent

class GpuDataProvider(
    private val activityManager: ActivityManager
) : KoinComponent {

    fun getGlEsVersion(): String {
        return activityManager.deviceConfigurationInfo.glEsVersion
    }
}
