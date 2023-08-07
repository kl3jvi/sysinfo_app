package com.kl3jvi.sysinfo.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences

@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}
