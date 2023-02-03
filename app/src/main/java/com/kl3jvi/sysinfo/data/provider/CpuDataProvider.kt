package com.kl3jvi.sysinfo.data.provider

import android.os.Build
import android.util.Log
import com.kl3jvi.sysinfo.data.model.CpuInfo
import com.kl3jvi.sysinfo.utils.cacheHumanReadable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileFilter
import java.io.RandomAccessFile
import java.util.regex.Pattern

class CpuDataProvider {
    external fun initLibrary()

    external fun getCpuName(): String

    external fun hasArmNeon(): Boolean

    external fun getL1dCaches(): IntArray?

    external fun getL1iCaches(): IntArray?

    external fun getL2Caches(): IntArray?

    external fun getL3Caches(): IntArray?

    external fun getL4Caches(): IntArray?

    private fun getAbi(): String {
        return Build.SUPPORTED_ABIS[0]
    }

    /**
     * This function returns the number of cores in the device, by checking the cpu[0-9]+ files in the `CPU_INFO_DIRECTORY`.
     *
     * @return The number of cores in the device, default is 1 if there is any exception while accessing the file system.
     */
    private fun getNumCoresLegacy(): Int {
        val cpuFilter = FileFilter { pathname -> Pattern.matches("cpu[0-9]+", pathname.name) }
        return try {
            File(CPU_INFO_DIRECTORY).listFiles(cpuFilter)?.size ?: 1
        } catch (e: Exception) {
            1
        }
    }

    private fun getNumberOfCores(): Int {
        return try {
            Runtime.getRuntime().availableProcessors()
        } catch (e: Exception) {
            getNumCoresLegacy()
        }
    }

    /**
     * Retrieve the current frequency of a CPU core.
     *
     * @param coreNumber The core number of the CPU to retrieve the frequency from.
     *
     * @return The current frequency in MHz, or -1 if it couldn't be retrieved.
     */
    private fun getCurrentFreq(coreNumber: Int): Long {
        val currentFreqPath = "${CPU_INFO_DIRECTORY}cpu$coreNumber/cpufreq/scaling_cur_freq"
        return try {
            RandomAccessFile(currentFreqPath, "r").use { it.readLine().toLong() / 1000 }
        } catch (e: Exception) {
            Log.e("getCurrentFreq()", "- cannot read file")
            -1
        }
    }

    /**
     * Retrieve the minimum and maximum frequency of a core.
     *
     * @param coreNumber the number of the core whose minimum and maximum frequency is to be retrieved.
     * @return Pair of minimum and maximum frequency in MHz.
     */
    private fun getMinMaxFreq(coreNumber: Int): Pair<Long, Long> {
        val minPath = "${CPU_INFO_DIRECTORY}cpu$coreNumber/cpufreq/cpuinfo_min_freq"
        val maxPath = "${CPU_INFO_DIRECTORY}cpu$coreNumber/cpufreq/cpuinfo_max_freq"
        return try {
            val minMhz = RandomAccessFile(minPath, "r").use { it.readLine().toLong() / 1000 }
            val maxMhz = RandomAccessFile(maxPath, "r").use { it.readLine().toLong() / 1000 }
            Pair(minMhz, maxMhz)
        } catch (e: Exception) {
            Log.e("getMinMaxFreq()", "- cannot read file")
            Pair(-1, -1)
        }
    }

    /**
     * Returns a flow of [CpuInfo] containing information about the device's CPU cores.
     *
     * The flow will emit an updated [CpuInfo] object after each [DELAY] interval. The flow will
     * contain the processor name, application binary interface (ABI), number of cores, presence of
     * ARM NEON, frequency information of each core, and cache information. The frequency information
     * will include the minimum, maximum, and current frequency of each core. The cache information
     * will include the human-readable representation of the level 1 data, level 1 instruction, level 2,
     * level 3, and level 4 caches.
     *
     * The flow uses `distinctUntilChanged` to emit only unique [CpuInfo] objects. The flow will run
     * on the IO dispatcher.
     *
     * @return a flow of [CpuInfo] containing CPU information.
     */
    fun getCpuCoresInformation(): Flow<CpuInfo> {
        val numberOfCores = getNumberOfCores()
        val minMaxFreq = (0 until numberOfCores).map { getMinMaxFreq(it) }
        val cpuName = getCpuName()
        val abi = getAbi()
        val hasArmNeon = hasArmNeon()
        val l1dCaches = getL1dCaches().cacheHumanReadable()
        val l1iCaches = getL1iCaches().cacheHumanReadable()
        val l2Caches = getL2Caches().cacheHumanReadable()
        val l3Caches = getL3Caches().cacheHumanReadable()
        val l4Caches = getL4Caches().cacheHumanReadable()

        return flow {
            while (true) {
                val coresData = (0 until numberOfCores).map { getCurrentFreq(it) }
                    .zip(minMaxFreq)
                    .map { (current, frequencyHolder) ->
                        val (minimum, maximum) = frequencyHolder
                        CpuInfo.Frequency(
                            min = minimum,
                            max = maximum,
                            current = current
                        )
                    }

                val cpuInfo = CpuInfo(
                    processorName = cpuName,
                    abi = abi,
                    coreNumber = numberOfCores,
                    hasArmNeon = hasArmNeon,
                    frequencies = coresData,
                    l1dCaches = l1dCaches,
                    l1iCaches = l1iCaches,
                    l2Caches = l2Caches,
                    l3Caches = l3Caches,
                    l4Caches = l4Caches
                )
                emit(cpuInfo)
                delay(DELAY)
            }
        }.distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }


    companion object {
        private const val CPU_INFO_DIRECTORY = "/sys/devices/system/cpu/"
        private const val DELAY = 1000L
    }
}
