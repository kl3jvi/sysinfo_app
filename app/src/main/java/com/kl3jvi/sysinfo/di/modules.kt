package com.kl3jvi.sysinfo.di

import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.SensorManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.view.WindowManager
import com.kl3jvi.sysinfo.data.provider.*
import com.kl3jvi.sysinfo.viewmodel.DashboardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

private val viewModelModule = module {
    viewModel { DashboardViewModel(get(), get(), get(), get()) }
}

private val providerModule = module {
    single { CpuDataProvider() }
    single { RamDataProvider(get()) }
    single { GpuDataProvider(get()) }
    single { StorageProvider(get()) }
    single { BatteryDataProvider(get()) }
}

private val appModule = module {
    single<Resources> { androidContext().resources }
    single { androidContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager }
    single { androidContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager }
    single<PackageManager> { androidContext().packageManager }
    single<ContentResolver> { androidContext().contentResolver }
    single { androidContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    single { androidContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    single { androidContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager }
    single { androidContext().getSystemService(Context.BATTERY_SERVICE) as BatteryManager }
}

val allModules = appModule + viewModelModule + providerModule
