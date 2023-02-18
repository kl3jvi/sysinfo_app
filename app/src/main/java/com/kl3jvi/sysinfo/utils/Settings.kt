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
        default = 5000L
    )

    val coreFrequencyRefreshRate by longPreference(
        appContext.getPreferenceKey(R.string.cpu_frequency_pref),
        default = 1000L
    )
}
