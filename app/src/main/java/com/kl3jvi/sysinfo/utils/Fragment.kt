package com.kl3jvi.sysinfo.utils

import android.app.Activity
import android.content.Intent
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment


/**
 * Sets the [WindowManager.LayoutParams.FLAG_SECURE] flag for the current activity window.
 */
fun Fragment.secure() {
    this.activity?.window?.addFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
    )
}

/**
 * Clears the [WindowManager.LayoutParams.FLAG_SECURE] flag for the current activity window.
 */
fun Fragment.removeSecure() {
    this.activity?.window?.clearFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
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
    onSuccess: (result: ActivityResult) -> Unit,
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onSuccess(result)
        } else {
            onFailure(result)
        }
    }
}