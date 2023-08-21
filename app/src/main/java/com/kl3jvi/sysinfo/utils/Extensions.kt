package com.kl3jvi.sysinfo.utils

import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

inline fun <reified T> Preference.setOnPreferenceChangeListener(
    crossinline onPreferenceChangeListener: (Preference, T) -> Boolean
) {
    setOnPreferenceChangeListener { preference: Preference, newValue: Any ->
        (newValue as? T)?.let { onPreferenceChangeListener(preference, it) } ?: false
    }
}

fun <T : Preference> PreferenceFragmentCompat.requirePreference(@StringRes preferenceId: Int) =
    requireNotNull(findPreference<T>(getPreferenceKey(preferenceId)))

fun <T : Preference> PreferenceFragmentCompat.configurePreference(
    @StringRes preferenceId: Int,
    preferences: SharedPreferences,
    clickListener: Preference.OnPreferenceClickListener? = null,
    block: T.() -> Unit = {}
): T {
    val preference = requirePreference<T>(preferenceId)
    preference.block()

    preference.setOnPreferenceChangeListener<Any> { _, newValue ->
        val key = getPreferenceKey(preferenceId)
        preferences.edit {
            Log.e("Changed Val", newValue.toString())
            when (newValue) {
                is Boolean -> putBoolean(key, newValue)
                is Int -> putLong(key, newValue.toLong() * 1000)
                is String -> putString(key, newValue)
            }
        }
        true
    }

    preference.onPreferenceClickListener = clickListener
    return preference
}