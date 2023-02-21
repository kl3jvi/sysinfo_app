package com.kl3jvi.sysinfo.di

import android.app.ActivityManager
import android.app.Application
import android.app.admin.DevicePolicyManager
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.SensorManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.view.WindowManager
import com.kl3jvi.sysinfo.data.provider.BatteryDataProvider
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.data.provider.GpuDataProvider
import com.kl3jvi.sysinfo.data.provider.RamDataProvider
import com.kl3jvi.sysinfo.data.provider.StorageProvider
import com.kl3jvi.sysinfo.utils.Settings
import com.kl3jvi.sysinfo.utils.WifiConnectionMonitor
import com.kl3jvi.sysinfo.viewmodel.DataViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val SYSINFO_PREFERENCES = "sysinfo_preferences"

private val viewModelModule = module {
    viewModel { DataViewModel(get(), get(), get(), get()) }
}

private val providerModule = module {
    single { CpuDataProvider(get()) }
    single { RamDataProvider(get(), get()) }
    single { GpuDataProvider(get()) }
    single { StorageProvider(get()) }
    single { BatteryDataProvider(get()) }
}

private val appModule = module {
    single<Resources> { androidContext().resources }
    single<ActivityManager> { androidContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager }
    single<DevicePolicyManager> { androidContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager }
    single<PackageManager> { androidContext().packageManager }
    single<ContentResolver> { androidContext().contentResolver }
    single<WindowManager> { androidContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    single<SensorManager> { androidContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    single<WifiManager> { androidContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager }
    single<BatteryManager> { androidContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager }
    single<WifiConnectionMonitor> { WifiConnectionMonitor(androidContext() as Application) }
}

private val persistenceModule = module {
    single { Settings(get(), get()) }
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            SYSINFO_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
}

val allModules = appModule +
        viewModelModule +
        providerModule +
        persistenceModule
