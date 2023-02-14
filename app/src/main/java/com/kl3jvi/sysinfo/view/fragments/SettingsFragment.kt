package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.utils.requirePreference

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupPreferences()
    }

    private fun setupPreferences() {
        requirePreference<SeekBarPreference>(R.string.ram_refresh_pref).apply {
            seekBarIncrement = 1000
        }
    }
}