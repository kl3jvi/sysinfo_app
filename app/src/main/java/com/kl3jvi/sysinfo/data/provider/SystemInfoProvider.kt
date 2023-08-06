package com.kl3jvi.sysinfo.data.provider

import android.app.ActivityManager
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import com.kl3jvi.sysinfo.domain.models.Information
import com.kl3jvi.sysinfo.utils.clearEmptyEntries
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class SystemInfoProvider(
    private val packageManager: PackageManager,
    private val activityManager: ActivityManager
) {

    private fun getAndroidVersion(): Information {
        return Information("Android Version", Build.VERSION.RELEASE)
    }

    private fun getSecurityPatchLevel(): Information {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Information("Security Patch Level", Build.VERSION.SECURITY_PATCH)
        } else {
            Information("Security Patch Level", "")
        }
    }

    private fun getBuildNumber(): Information {
        return Information("Build Number", Build.DISPLAY)
    }

    private fun getBaseband(): Information {
        return Information("Baseband", Build.getRadioVersion())
    }

    private fun getJavaVM(): Information {
        return Information("Java VM", System.getProperty("java.vm.version") ?: "N/A")
    }

    private fun getKernel(): Information {
        return Information("Kernel", System.getProperty("os.version") ?: "N/A")
    }

    private fun getOpenGL(glesVersion: String): Information {
        return Information("OpenGL ES", glesVersion)
    }

    private fun getRootAccess(): Information {
        val isRooted = findBinary("su")
        return Information("Root Access", if (isRooted) "Yes" else "No")
    }

    private fun findBinary(binaryName: String): Boolean {
        val paths = System.getenv("PATH")?.split(":") ?: emptyList()
        return paths.any { File(it, binaryName).exists() }
    }

    private fun getSELinux(): Information {
        return Information(
            "SE Linux",
//            try {
//                val process = Runtime.getRuntime().exec("getenforce")
//                val reader = BufferedReader(InputStreamReader(process.inputStream))
//                val result = reader.readLine()
//                reader.close()
//                result
//            } catch (e: IOException) {
//                "N/A"
//            }
            ""
        )
    }

    private fun getGooglePlayServices(): Information {
        return try {
            val pInfo: PackageInfo = packageManager.getPackageInfo("com.google.android.gms", 0)
            Information("Google Play Services", pInfo.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            Information("Google Play Services", "Not Installed")
        }
    }

    fun getSystemUptimeFlow(): Flow<Information> = flow {
        while (true) {
            val uptime = SystemClock.elapsedRealtime()
            val hours = TimeUnit.MILLISECONDS.toHours(uptime)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(uptime) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(uptime) % 60

            val currentTime = "%02d:%02d:%02d".format(hours, minutes, seconds)
            emit(Information("System Uptime", currentTime))
            delay(1000L)
        }
    }

    private fun getTreble(): Information {
        return Information(
            "Treble",
            try {
                val process = Runtime.getRuntime().exec("getprop ro.treble.enabled")
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val result = reader.readLine()
                reader.close()
                if (result == "true") "Enabled" else "Disabled"
            } catch (e: IOException) {
                "N/A"
            }
        )
    }

    private fun getGlEsVersion(): String {
        return activityManager.deviceConfigurationInfo.glEsVersion
    }

    fun getSystemInfo(): List<Information> {
        return try {
            listOf(
                getAndroidVersion(),
                getSecurityPatchLevel(),
                getBuildNumber(),
                getBaseband(),
                getJavaVM(),
                getKernel(),
                getOpenGL(getGlEsVersion()),
                getRootAccess(),
                getSELinux(),
                getGooglePlayServices(),
                getTreble()
                // Include other information functions here if needed
            ).clearEmptyEntries()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
