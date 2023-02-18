package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.utils.Settings
import com.kl3jvi.sysinfo.utils.edit
import com.kl3jvi.sysinfo.utils.getPreferenceKey
import com.kl3jvi.sysinfo.utils.requirePreference
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsFragment : PreferenceFragmentCompat(), KoinComponent {

    private val settings: Settings by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupPreferences()
    }

    private fun setupPreferences() {
        requirePreference<SeekBarPreference>(R.string.ram_refresh_pref).apply {
            seekBarIncrement = 1000
            summary = String.format("${settings.ramRefreshRate}")
            onPreferenceChangeListener = object : Preference.OnPreferenceChangeListener {
                override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
                    settings.preferences.edit {
                        putLong(getPreferenceKey(R.string.ram_refresh_pref), (newValue as Int).times(1000).toLong())
                    }
                    preference.summary = String.format("${newValue}000")
                    return true
                }
            }
        }
    }
}