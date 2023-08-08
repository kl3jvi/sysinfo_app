package com.kl3jvi.sysinfo.view.fragments

import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import com.example.sysinfo.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kl3jvi.sysinfo.utils.Settings
import com.kl3jvi.sysinfo.utils.configurePreference
import com.kl3jvi.sysinfo.utils.edit
import com.kl3jvi.sysinfo.utils.getPreferenceKey
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsFragment : PreferenceFragmentCompat(), KoinComponent {

    private val settings: Settings by inject()
    private var preference: SwitchPreferenceCompat? = null
    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::dialog
    )

    private fun dialog(isGranted: Boolean) {
        val title: String
        val message: String

        settings.preferences.edit {
            putBoolean(
                requireContext().getPreferenceKey(R.string.enable_monitoring_pref),
                isGranted
            )
        }
        preference?.isChecked = isGranted && settings.systemMonitoringState

        if (isGranted) {
            title = "Permission Granted"
            message = "Thank you! System monitoring will now send notifications."
        } else {
            title = "Permission Denied"
            message =
                "System monitoring will not be able to send notifications without the required permission. You can enable it from the system settings."
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupPreferences()
    }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun setupPreferences() {
        configurePreference<SeekBarPreference>(
            preferenceId = R.string.ram_refresh_pref,
            settings.preferences
        )

        configurePreference<SeekBarPreference>(
            R.string.cpu_frequency_pref,
            settings.preferences
        )

        preference = configurePreference<SwitchPreferenceCompat>(
            R.string.enable_monitoring_pref,
            settings.preferences,
            clickListener = {
                if (settings.systemMonitoringState) askForPermission()
                true
            }
        )

        configurePreference<SeekBarPreference>(
            R.string.monitoring_period,
            settings.preferences
        )

    }
}
