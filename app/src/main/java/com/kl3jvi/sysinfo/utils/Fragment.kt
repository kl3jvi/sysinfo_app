package com.kl3jvi.sysinfo.utils

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

/**
 * Sets the [WindowManager.LayoutParams.FLAG_SECURE] flag for the current activity window.
 */
fun Fragment.secure() {
    this.activity?.window?.addFlags(
        WindowManager.LayoutParams.FLAG_SECURE
    )
}

/**
 * Clears the [WindowManager.LayoutParams.FLAG_SECURE] flag for the current activity window.
 */
fun Fragment.removeSecure() {
    this.activity?.window?.clearFlags(
        WindowManager.LayoutParams.FLAG_SECURE
    )
}

/**
 * Registers a fragment to receive activity result through a [ActivityResultLauncher].
 *
 * @param onFailure A callback to be invoked if the resultCode is not `Activity.RESULT_OK`.
 *                  The default implementation does nothing.
 * @param onSuccess A callback to be invoked if the resultCode is `Activity.RESULT_OK`.
 *
 * @return An [ActivityResultLauncher] that can be used to start an activity for result.
 */
fun Fragment.registerForActivityResult(
    onFailure: (result: ActivityResult) -> Unit = {},
    onSuccess: (result: ActivityResult) -> Unit
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onSuccess(result)
        } else {
            onFailure(result)
        }
    }
}

fun Fragment.getPreferenceKey(@StringRes resourceId: Int): String = getString(resourceId)

fun Fragment.setupActionBar(
    @MenuRes layout: Int,
    itemSelected: (MenuItem) -> Unit
) {
    requireActivity().addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            // Add menu items here
            menuInflater.inflate(layout, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            itemSelected(menuItem)
            return true
        }
    })
}

fun Fragment.nav(@IdRes id: Int?, directions: NavDirections, options: NavOptions? = null) {
    findNavController().nav(id, directions, options)
}
