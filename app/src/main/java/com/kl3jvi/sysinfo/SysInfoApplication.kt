package com.kl3jvi.sysinfo

import android.app.Application
import android.util.Log
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

fun <T : Any> T.then(block: () -> Unit): Result<Unit> {
    // The extension function that takes a block as a parameter and runs it after the execution of the last Unit method
    return runCatching(block)
}
