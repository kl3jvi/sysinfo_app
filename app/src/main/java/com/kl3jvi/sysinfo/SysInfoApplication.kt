package com.kl3jvi.sysinfo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.getkeepsafe.relinker.ReLinker
import com.kl3jvi.sysinfo.data.model.ChannelData
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

    // Nice to Have :)
    fun ensureNotificationChannelExists(
        context: Context,
        channelDate: ChannelData,
        onSetupChannel: NotificationChannel.() -> Unit = {},
        onCreateChannel: NotificationManager.() -> Unit = {},
    ): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelDate.id,
                context.getString(channelDate.name),
                channelDate.importance,
            )
            onSetupChannel(channel)
            notificationManager.createNotificationChannel(channel)
            onCreateChannel(notificationManager)
        }

        return channelDate.id
    }

    companion object {
        private const val CHANNEL_ID = "SystemMonitorChannel"
        private const val CHANNEL_NAME = "System Monitor"
        private const val CHANNEL_DESC = "Notifications for system monitoring"

        const val LIB_NAME = "cpuinfo-libs"
    }
}

const val DEFAULT_FONT_PADDING = 6

fun TextView.adjustMaxTextSize(
    heightMeasureSpec: Int,
    ascenderPadding: Int = DEFAULT_FONT_PADDING
) {
    val maxHeight = View.MeasureSpec.getSize(heightMeasureSpec)

    var availableHeight = maxHeight.toFloat()
    if (this.includeFontPadding) {
        availableHeight -= ascenderPadding * resources.displayMetrics.density
    }

    availableHeight -= (this.paddingBottom + this.paddingTop) *
            resources.displayMetrics.density

    if (availableHeight > 0 && this.textSize > availableHeight) {
        this.textSize = availableHeight / resources.displayMetrics.density
    }
}