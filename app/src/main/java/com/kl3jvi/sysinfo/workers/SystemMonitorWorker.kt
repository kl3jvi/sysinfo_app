package com.kl3jvi.sysinfo.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.data.model.CpuInfo
import com.kl3jvi.sysinfo.data.model.RamInfo
import com.kl3jvi.sysinfo.data.provider.CPULoad
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.data.provider.RamDataProvider
import com.kl3jvi.sysinfo.utils.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SystemMonitorWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {
    // Get dependencies via Koin
    private val ramDataProvider: RamDataProvider by inject()
    private val cpuDataProvider: CpuDataProvider by inject()
    private val notificationManager: NotificationManager by inject()
    private val settings: Settings by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        // Here we check the current RAM and CPU stats
        val monitoringPeriod = 60000 // one minute
        val cpuRefreshFrequency = settings.coreFrequencyRefreshRate
        val ramRefreshFrequency = settings.ramRefreshRate

        val numberOfSamplesForCpu = (monitoringPeriod / cpuRefreshFrequency).toInt()
        val numberOfSamplesForRam = (monitoringPeriod / ramRefreshFrequency).toInt()

        val ramInfo = ramDataProvider.getRamInformation()
            .scan(emptyList<RamInfo>()) { acc, value -> (acc + value).takeLast(numberOfSamplesForCpu) }
            .take(numberOfSamplesForCpu)
            .toList()
            .flatten()

        val cpuInfo = cpuDataProvider.getCpuCoresInformation()
            .scan(emptyList<CpuInfo>()) { acc, value -> (acc + value).takeLast(numberOfSamplesForRam) }
            .take(numberOfSamplesForRam)
            .toList()
            .flatten()

        // Check if thresholds are exceeded and trigger a notification if they are
        if (shouldTriggerNotification(ramInfo, cpuInfo)) {
            showNotification()
        }

        // Indicate whether the work finished successfully with the Result
        return@withContext Result.success()
    }

    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                setBypassDnd(true)
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("System Alert")
            .setContentText("Your CPU and RAM usage is high.")
            .setSmallIcon(R.drawable.alert_icon) // replace with your own icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun shouldTriggerNotification(ramInfo: List<RamInfo>, cpuInfo: List<CpuInfo>): Boolean {
        // If our sample has any percentage less than 20% something is eating ram!!
        val ramThresholdExceeded = ramInfo.any { it.percentageAvailable < 20 }
        // If our sample has in all of them any occurence of a high cpu load chances are that
        // something really heavy is running
        val cpuLoadHigh =
            cpuInfo.all { it.frequencies.any { frequency -> frequency.cpuLoad == CPULoad.High } }

        return ramThresholdExceeded && cpuLoadHigh
    }

    companion object {
        private const val CHANNEL_ID = "SystemMonitorChannel"
        private const val CHANNEL_NAME = "System Monitor"
        private const val CHANNEL_DESC = "Notifications for system monitoring"
        private const val NOTIFICATION_ID = 1
    }
}
