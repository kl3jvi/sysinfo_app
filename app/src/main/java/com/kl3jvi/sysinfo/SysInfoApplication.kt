package com.kl3jvi.sysinfo

import android.app.Application
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.getkeepsafe.relinker.ReLinker
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.di.allModules
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

    override fun onCreate() {
        super.onCreate()
        startKoin()
        initNativeCpuInfo()
        addSystemMonitor()
        addMSApp()
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
        val workRequest = PeriodicWorkRequestBuilder<SystemMonitorWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun startKoin() = startKoin {
        androidContext(this@SysInfoApplication)
        modules(allModules)
    }

    private fun initNativeCpuInfo() {
        ReLinker.loadLibrary(this, LIB_NAME)
            .thenCatching {
                cpuDataProvider.initLibrary()
            }.onSuccess {
                Log.i("Initialised CpuInfo", "successfully")
            }.onFailure {
                Log.e("Failed cpu-info", "initialisation", it)
            }
    }

    companion object {
        const val LIB_NAME = "cpuinfo-libs"
    }
}
