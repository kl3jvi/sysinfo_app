package com.kl3jvi.sysinfo

import android.app.Application
import android.util.Log
import com.getkeepsafe.relinker.ReLinker
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.di.allModules
import com.kl3jvi.sysinfo.utils.then
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class SysInfoApplication : Application(), KoinComponent {
    private val cpuDataProvider: CpuDataProvider by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin()
        initNativeCpuInfo()
    }

    private fun startKoin() = startKoin {
        androidContext(this@SysInfoApplication)
        modules(allModules)
    }

    private fun initNativeCpuInfo() {
        ReLinker.loadLibrary(this, LIB_NAME).then {
            cpuDataProvider.initLibrary()
        }.onSuccess {
            Log.e("Initialised CpuInfo", "successfully")
        }.onFailure {
            Log.e("Failed cpu-info", "initialisation")
        }
    }

    companion object {
        const val LIB_NAME = "cpuinfo-libs"
    }
}
