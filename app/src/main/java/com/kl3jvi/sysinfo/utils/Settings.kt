package com.kl3jvi.sysinfo.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.data.local.PreferencesHolder
import com.kl3jvi.sysinfo.data.local.intPreference
import com.kl3jvi.sysinfo.data.local.longPreference

class Settings(
    private val appContext: Context
) : PreferencesHolder {

    companion object {
        const val SYSINFO_PREFERENCES = "sysinfo_preferences"
    }

    override val preferences: SharedPreferences
        get() = appContext.getSharedPreferences(SYSINFO_PREFERENCES, MODE_PRIVATE)


    val ramRefreshRate by intPreference(
        appContext.getPreferenceKey(R.string.ram_refresh_pref),
        default = 0
    )

    val coreFrequencyRefreshRate by longPreference(
        appContext.getPreferenceKey(R.string.cpu_frequency_pref),
        default = 0L
    )
}