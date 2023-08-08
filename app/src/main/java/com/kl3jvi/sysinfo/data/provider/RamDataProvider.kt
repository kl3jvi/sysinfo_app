package com.kl3jvi.sysinfo.data.provider

import android.app.ActivityManager
import com.kl3jvi.sysinfo.data.model.RamInfo
import com.kl3jvi.sysinfo.data.model.RamLoad
import com.kl3jvi.sysinfo.utils.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent

class RamDataProvider(
    private val activityManager: ActivityManager,
    private val settings: Settings
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

    private fun getRamLoad(): RamLoad {
        return when (getAvailablePercentage()) {
            in 0..25 -> RamLoad.High
            in 26..50 -> RamLoad.Medium
            else -> RamLoad.Low
        }
    }

    fun getRamInformation(): Flow<RamInfo> = flow {
        while (true) {
            val total = getTotalBytes()
            val available = getAvailableBytes()
            val availablePercentage = getAvailablePercentage()
            val threshold = getThreshold()
            val ramLoad = getRamLoad()
            emit(RamInfo(total, available, availablePercentage, threshold, ramLoad))
            delay(settings.ramRefreshRate)
        }
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
}


