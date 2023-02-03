package com.kl3jvi.sysinfo.data.provider

import android.app.ActivityManager
import org.koin.core.component.KoinComponent

class GpuDataProvider(
    private val activityManager: ActivityManager
) : KoinComponent {

    /**
     * Returns the GLES version of the device as a string.
     *
     * @return A string representation of the GLES version on the device.
     */
    fun getGlEsVersion(): String {
        return activityManager.deviceConfigurationInfo.glEsVersion
    }
}
