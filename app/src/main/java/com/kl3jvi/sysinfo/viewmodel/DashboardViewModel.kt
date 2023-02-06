package com.kl3jvi.sysinfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.sysinfo.data.model.RamInfo
import com.kl3jvi.sysinfo.data.model.toHumanReadableValues
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.data.provider.RamDataProvider
import com.kl3jvi.sysinfo.utils.UiResult
import com.kl3jvi.sysinfo.utils.humanReadableByteCount
import com.kl3jvi.sysinfo.utils.invoke
import com.kl3jvi.sysinfo.utils.mapToUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    cpuDataProvider: CpuDataProvider,
    ramDataProvider: RamDataProvider
) : ViewModel() {

    val cpuInfo = cpuDataProvider
        .getCpuCoresInformation()
        .mapToUiState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiResult.Idle
        )

    val ramInfo = ramDataProvider
        .getRamInformation()
        .map {
            it.toHumanReadableValues()
        }
        .mapToUiState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiResult.Idle
        )
}
