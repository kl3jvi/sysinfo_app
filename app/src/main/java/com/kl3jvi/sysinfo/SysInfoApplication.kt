package com.kl3jvi.sysinfo

import android.app.Application
import com.getkeepsafe.relinker.ReLinker
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class SysInfoApplication : Application(), KoinComponent {
    private val cpuDataProvider: CpuDataProvider by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SysInfoApplication)
            modules(allModules)
        }
        initNativeCpuInfo()
    }

    private fun initNativeCpuInfo() {
        ReLinker.loadLibrary(this, "cpuinfo-libs")
        cpuDataProvider.initLibrary()
    }
}
