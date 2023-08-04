package com.kl3jvi.sysinfo.utils

import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

/**
 * Navigate from the fragment with [id] using the given [directions].
 * If the id doesn't match the current destination, an error is recorded.
 */
fun NavController.nav(@IdRes id: Int?, directions: NavDirections, navOptions: NavOptions? = null) {
    if (id == null || this.currentDestination?.id == id) {
        this.navigate(directions, navOptions)
    } else {
        Log.e("Fragment id ${this.currentDestination?.id}", "did not match expected $id")
    }
}

fun NavController.alreadyOnDestination(@IdRes destId: Int?): Boolean {
    return destId?.let { currentDestination?.id == it || popBackStack(it, false) } ?: false
}

fun NavController.navigateSafe(
    @IdRes resId: Int,
    directions: NavDirections
) {
    if (currentDestination?.id == resId) {
        this.navigate(directions)
    }
}

