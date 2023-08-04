package com.kl3jvi.sysinfo.data.provider

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import com.kl3jvi.sysinfo.domain.models.Information
import org.koin.core.component.KoinComponent

class DeviceDataProvider(
    private val appContext: Context,
    private val contentResolver: ContentResolver,
    private val wifiManager: WifiManager
) : KoinComponent {

    fun getDeviceInformation(): List<Information> {
        return listOf(
            Information("Device Name", Build.DEVICE),
            Information("Device Model", Build.MODEL),
            Information("Manufacturer", Build.MANUFACTURER),
            Information("Device", Build.DEVICE),
            Information("Board", Build.BOARD),
            Information("Hardware", Build.HARDWARE),
            Information("Brand", Build.BRAND),
            Information(
                "Android Device Id",
                Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            ),
            Information(
                "Language",
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) appContext.resources.configuration.locales[0].displayName else ""
            ),
            Information("Build Fingerprint", Build.FINGERPRINT),
            Information(
                "Usb Host",
                if (appContext.packageManager.hasSystemFeature(PackageManager.FEATURE_USB_HOST)) "Supported" else "Not Supported"
            ),
            Information(
                "Device Type",
                if (appContext.resources.configuration.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) "Tablet" else "Phone"
            ),
            Information("Network Type", getNetworkType()),
            Information("Main Network Operator", getMainNetworkOperator().getOrNull() ?: "Unknown")
        )
    }

    private fun getNetworkType(): String {
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return "Unknown"
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return "Unknown"

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile"
            else -> "Unknown"
        }
    }

    private fun getMainNetworkOperator(): Result<String> {
        return runCatching { wifiManager.connectionInfo.ssid }
    }
}
