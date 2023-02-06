package com.kl3jvi.sysinfo.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi

fun Context.showToast(message: String) {
    val currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    if ((currentToast.view?.windowVisibility ?: false) != View.VISIBLE) {
        currentToast.show()
    }
}
