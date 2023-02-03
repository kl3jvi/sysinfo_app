package com.kl3jvi.sysinfo.data.provider

import android.app.ActivityManager
import com.kl3jvi.sysinfo.data.model.RamInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class RamDataProvider(
    private val activityManager: ActivityManager
) : KoinComponent {

    private fun getTotalBytes(): Long {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem
    }

    private fun getAvailableBytes(): Long {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    private fun getAvailablePercentage(): Int {
        val total = getTotalBytes().toDouble()
        val available = getAvailableBytes().toDouble()
        return (available / total * 100).toInt()
    }

    private fun getThreshold(): Long {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.threshold
    }

    fun getRamInformation() = flow {
        while (true) {
            val total = getTotalBytes()
            val available = getAvailableBytes()
            val availablePercentage = getAvailablePercentage()
            val threshold = getThreshold()
            emit(RamInfo(total, available, availablePercentage, threshold))
            delay(REFRESH_DELAY)
        }
    }

    companion object {
        private const val REFRESH_DELAY = 5000L
    }
}