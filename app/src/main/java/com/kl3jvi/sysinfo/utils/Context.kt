package com.kl3jvi.sysinfo.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.view.ContextThemeWrapper

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.asActivity() = (this as? ContextThemeWrapper)?.baseContext as? Activity
    ?: this as? Activity

fun Context.getPreferenceKey(@StringRes resourceId: Int): String =
    resources.getString(resourceId)
