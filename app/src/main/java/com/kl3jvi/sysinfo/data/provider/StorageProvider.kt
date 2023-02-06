package com.kl3jvi.sysinfo.data.provider

import android.content.Context
import android.os.StatFs
import org.koin.core.component.KoinComponent

class StorageProvider(
    private val context: Context
) : KoinComponent {

    private fun getSystemStorageUsage(): Pair<Long, Long> {
        val root = "/"
        val stat = StatFs(root)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong
        val totalStorage = blockSize * totalBlocks
        val availableStorage = blockSize * availableBlocks
        return Pair(totalStorage, totalStorage - availableStorage)
    }

    private fun getInternalStorageUsage(): Pair<Long, Long> {
        val internalStorage = context.filesDir.absolutePath
        val internalStat = StatFs(internalStorage)
        val internalBlockSize = internalStat.blockSizeLong
        val internalTotalBlocks = internalStat.blockCountLong
        val internalAvailableBlocks = internalStat.availableBlocksLong
        val totalInternalStorage = internalBlockSize * internalTotalBlocks
        val availableInternalStorage = internalBlockSize * internalAvailableBlocks
        return Pair(totalInternalStorage, totalInternalStorage - availableInternalStorage)
    }

    fun calculateSystemPercentage(): Int {
        val (total, available) = getSystemStorageUsage()
        return (available / total * 100).toInt()
    }

    fun calculateInternalPercentage(): Int {
        val (total, available) = getInternalStorageUsage()
        return (available.toDouble() / total * 100).toInt()
    }
}
