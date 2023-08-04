package com.kl3jvi.sysinfo.data.provider

import android.app.ActivityManager
import android.content.pm.PackageManager
import android.os.Build
import org.koin.core.component.KoinComponent

class GpuDataProvider(
    private val activityManager: ActivityManager,
    private val packageManager: PackageManager
) : KoinComponent {

    /**
     * Returns the GLES version of the device as a string.
     *
     * @return A string representation of the GLES version on the device.
     */
    fun getGlEsVersion(): String {
        return activityManager.deviceConfigurationInfo.glEsVersion
    }

    fun getVulkanVersion(): String {
        val default = "Unknown"
        if (Build.VERSION.SDK_INT < 24) {
            return default
        }

        val vulkan = packageManager.systemAvailableFeatures.find {
            it.name == PackageManager.FEATURE_VULKAN_HARDWARE_VERSION
        }?.version ?: 0
        if (vulkan == 0) {
            return default
        }

        // Extract versions from bit field
        // See: https://developer.android.com/reference/android/content/pm/PackageManager#FEATURE_VULKAN_HARDWARE_VERSION
        val major = vulkan shr 22 // Higher 10 bits
        val minor = vulkan shl 10 shr 22 // Middle 10 bits
        val patch = vulkan shl 20 shr 22 // Lower 12 bits
        //
        return "$major.$minor.$patch"
    }
}
