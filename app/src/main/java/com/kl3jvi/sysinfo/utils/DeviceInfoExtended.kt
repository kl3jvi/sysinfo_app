package com.kl3jvi.sysinfo.utils

import android.app.ActivityManager
import android.content.Context
import com.an.deviceinfo.device.model.Memory
import java.io.File
import java.io.RandomAccessFile
import java.util.regex.Pattern

class DeviceInfoExtended(private val context: Context) : Memory(context) {

    fun getAvailableRam(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        return mi.availMem / 1048576L
    }

    fun getMaxCPUFreq(core: Int): Int {
        var currentFReq = 0
        try {
            val currentFreq: Double
            val readerCurFreq: RandomAccessFile =
                RandomAccessFile("/sys/devices/system/cpu/cpu$core/cpufreq/cpuinfo_max_freq", "r")
            val currentfreq = readerCurFreq.readLine()
            currentFreq = currentfreq.toDouble() / 1000
            readerCurFreq.close()
            currentFReq = currentFreq.toInt()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return currentFReq
    }

    fun getNumOfCores(): Int {
        return File("/sys/devices/system/cpu/").listFiles { params ->
            Pattern.matches(
                "cpu[0-9]",
                params.name
            )
        }.size
    }

    fun totalDeviceRam(): Long {
        return totalRAM / 1048576L
    }
}