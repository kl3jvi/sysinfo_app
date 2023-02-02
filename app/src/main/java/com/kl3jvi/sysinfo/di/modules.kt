package com.kl3jvi.sysinfo.di

import com.kl3jvi.sysinfo.data.provider.CpuNativeData
import com.kl3jvi.sysinfo.viewmodel.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
}

private val providerModule = module {
    single { CpuNativeData() }
}

val allModules = viewModelModule + providerModule
