package com.kl3jvi.sysinfo.utils

import android.app.ActivityManager
import android.content.Context
import com.an.deviceinfo.device.model.Memory

class MemoryExtended(private val context: Context) : Memory(context) {

    fun getAvailableRam(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        return mi.availMem / 1048576L
    }



}