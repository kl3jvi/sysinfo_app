package com.kl3jvi.sysinfo.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.data.local.PreferencesHolder
import com.kl3jvi.sysinfo.data.local.intPreference
import com.kl3jvi.sysinfo.data.local.longPreference

class Settings(
    appContext: Context,
    override val preferences: SharedPreferences
) : PreferencesHolder {

    val ramRefreshRate by longPreference(
        appContext.getPreferenceKey(R.string.ram_refresh_pref),
        default = RAM_REFRESH_RATE_DEFAULT
    )

    val coreFrequencyRefreshRate by longPreference(
        appContext.getPreferenceKey(R.string.cpu_frequency_pref),
        default = CPU_REFRESH_RATE_DEFAULT
    )

    val ramWarningThreshold by intPreference(
        appContext.getPreferenceKey(R.string.cpu_frequency_pref),
        default = RAM_WARNING_FREQUENCY_DEFAULT
    )

    companion object {
        const val RAM_REFRESH_RATE_DEFAULT = 5000L
        const val CPU_REFRESH_RATE_DEFAULT = 2000L

        const val RAM_WARNING_FREQUENCY_DEFAULT = 40
    }
}
