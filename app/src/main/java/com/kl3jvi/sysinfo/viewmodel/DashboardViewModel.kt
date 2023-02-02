package com.kl3jvi.sysinfo.viewmodel

import androidx.lifecycle.ViewModel
import com.kl3jvi.sysinfo.data.provider.CpuNativeData

class DashboardViewModel(
    private val cpuNativeData: CpuNativeData
) : ViewModel()
