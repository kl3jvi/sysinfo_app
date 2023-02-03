package com.kl3jvi.sysinfo.data.provider

import android.os.Build
import android.util.Log
import com.kl3jvi.sysinfo.data.model.CpuInfo
import com.kl3jvi.sysinfo.utils.cacheHumanReadable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    fun getAbi(): String {
        return Build.SUPPORTED_ABIS[0]
    }

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

    private fun getCurrentFreq(coreNumber: Int): Long {
        val currentFreqPath = "${CPU_INFO_DIRECTORY}cpu$coreNumber/cpufreq/scaling_cur_freq"
        return try {
            RandomAccessFile(currentFreqPath, "r").use { it.readLine().toLong() / 1000 }
        } catch (e: Exception) {
            Log.e("getCurrentFreq()", "- cannot read file")
            -1
        }
    }

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
     * Returns a flow that emits a list of [CoreInfo] objects each second, representing the current frequency,
     * minimum frequency, and maximum frequency of each core on the device.
     *
     * @return a flow of list of [CoreInfo] objects.
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
                        CpuInfo.Frequency(
                            min = frequencyHolder.first,
                            max = frequencyHolder.second,
                            current = current
                        )
                    }

                val result = CpuInfo(
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
                emit(result)
                delay(DELAY)
            }
        }
    }


    companion object {
        private const val CPU_INFO_DIRECTORY = "/sys/devices/system/cpu/"
        private const val DELAY = 1000L
    }
}
