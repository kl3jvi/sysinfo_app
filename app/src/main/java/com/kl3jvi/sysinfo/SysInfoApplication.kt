package com.kl3jvi.sysinfo

import android.app.Application
import com.kl3jvi.sysinfo.di.allModules
import org.koin.core.context.startKoin

class SysInfoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(allModules)
        }
    }
}
