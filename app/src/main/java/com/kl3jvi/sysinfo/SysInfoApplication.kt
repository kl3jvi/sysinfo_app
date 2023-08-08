package com.kl3jvi.sysinfo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.getkeepsafe.relinker.ReLinker
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.di.allModules
import com.kl3jvi.sysinfo.utils.Settings
import com.kl3jvi.sysinfo.utils.thenCatching
import com.kl3jvi.sysinfo.workers.SystemMonitorWorker
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class SysInfoApplication : Application(), KoinComponent {
    private val cpuDataProvider: CpuDataProvider by inject()
    private val notificationManager: NotificationManager by inject()
    private val settings: Settings by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin()
        initNativeCpuInfo()
        addSystemMonitor()
        addMSApp()
        createNotificationChannel()
    }

    private fun addMSApp() {
        AppCenter.start(
            this,
            "383ff08e-cd63-4dd2-8635-cb81239175f5",
            Analytics::class.java,
            Crashes::class.java
        )
    }

    private fun addSystemMonitor() {
        val shouldMonitor = settings.systemMonitoringState
        if (shouldMonitor) {
            val workRequest = PeriodicWorkRequestBuilder<SystemMonitorWorker>(
                15,
                TimeUnit.MINUTES
            ).build()
            WorkManager.getInstance(this).enqueue(workRequest)
        }
    }

    private fun startKoin() = startKoin {
        androidContext(this@SysInfoApplication)
        modules(allModules)
    }

    private fun initNativeCpuInfo() {
        ReLinker.loadLibrary(this, LIB_NAME).thenCatching {
            cpuDataProvider.initLibrary()
        }.onSuccess {
            Log.i("Initialised CpuInfo", "successfully")
        }.onFailure {
            Log.e("Failed cpu-info", "initialisation", it)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only for API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME // Define these strings in your res/values/strings.xml
            val descriptionText = CHANNEL_DESC
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                setBypassDnd(true)
                setShowBadge(true)
            }

            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "SystemMonitorChannel"
        private const val CHANNEL_NAME = "System Monitor"
        private const val CHANNEL_DESC = "Notifications for system monitoring"

        const val LIB_NAME = "cpuinfo-libs"
    }
}
