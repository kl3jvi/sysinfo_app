package com.kl3jvi.sysinfo.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.data.model.CPULoad
import com.kl3jvi.sysinfo.data.model.CpuInfo
import com.kl3jvi.sysinfo.data.model.RamInfo
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.data.provider.RamDataProvider
import com.kl3jvi.sysinfo.utils.Settings
import com.kl3jvi.sysinfo.utils.scanAndLimit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SystemMonitorWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {

    private val ramDataProvider: RamDataProvider by inject()
    private val cpuDataProvider: CpuDataProvider by inject()
    private val notificationManager: NotificationManager by inject()
    private val settings: Settings by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        try {
            val monitoringPeriod = settings.monitoringPeriod
            val cpuRefreshFrequency = settings.coreFrequencyRefreshRate
            val ramRefreshFrequency = settings.ramRefreshRate

            val numberOfSamplesForCpu = (monitoringPeriod / cpuRefreshFrequency).toInt()
            val numberOfSamplesForRam = (monitoringPeriod / ramRefreshFrequency).toInt()

            val ramInfo = collectRamData(numberOfSamplesForRam)
            val cpuInfo = collectCpuData(numberOfSamplesForCpu)

            checkAndTriggerNotification(ramInfo, cpuInfo)

            return@withContext Result.success()
        } catch (e: Exception) {
            Log.e("SystemMonitorWorker", "Error occurred during system monitoring.", e)
            return@withContext Result.failure()
        }
    }

    private suspend fun collectRamData(samples: Int): List<RamInfo> =
        ramDataProvider.getRamInformation().scanAndLimit(samples)

    private suspend fun collectCpuData(samples: Int): List<CpuInfo> =
        cpuDataProvider.getCpuCoresInformation().scanAndLimit(samples)

    private fun checkAndTriggerNotification(ramInfo: List<RamInfo>, cpuInfo: List<CpuInfo>) {
        val ramThreshold = settings.ramThresholdPercentage
        val ramThresholdExceeded = ramInfo.any { it.percentageAvailable < ramThreshold }

        if (ramThresholdExceeded) {
            showNotification(RAM_TITLE, RAM_MESSAGE)
        }

        val cpuLoadHigh = cpuInfo.all {
            it.frequencies.any { frequency ->
                frequency.cpuLoad == CPULoad.High
            }
        }
        if (cpuLoadHigh) {
            showNotification(CPU_TITLE, CPU_MESSAGE)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notification =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.alert_icon) // replace with your own icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val RAM_TITLE = "RAM Alert"
        private const val RAM_MESSAGE = "Your RAM usage is high."
        private const val NOTIFICATION_ID = 1

        private const val CPU_TITLE = "CPU Alert"
        private const val CPU_MESSAGE = "Your CPU load is high."
        const val CHANNEL_ID = "SystemMonitorChannel"
    }
}
