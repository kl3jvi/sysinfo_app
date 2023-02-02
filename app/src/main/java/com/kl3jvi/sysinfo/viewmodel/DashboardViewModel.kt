package com.kl3jvi.sysinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.sysinfo.data.model.RamInfo
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.data.provider.RamDataProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val cpuDataProvider: CpuDataProvider,
    ramDataProvider: RamDataProvider
) : ViewModel() {

    val ramInfo = ramDataProvider
        .getRamInformation()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            RamInfo()
        )

    val cpuInfo = cpuDataProvider
        .getCpuCoresInformation()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
}
