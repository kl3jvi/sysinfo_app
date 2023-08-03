package com.kl3jvi.sysinfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.sysinfo.data.model.CpuInfo
import com.kl3jvi.sysinfo.data.model.RamInfo
import com.kl3jvi.sysinfo.data.model.toDomainModel
import com.kl3jvi.sysinfo.data.provider.BatteryDataProvider
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.data.provider.RamDataProvider
import com.kl3jvi.sysinfo.data.provider.StorageProvider
import com.kl3jvi.sysinfo.utils.ifChanged
import com.kl3jvi.sysinfo.utils.mapToUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class DataViewModel(
    cpuDataProvider: CpuDataProvider,
    ramDataProvider: RamDataProvider,
    storageProvider: StorageProvider,
    batteryDataProvider: BatteryDataProvider
) : ViewModel() {

    val systemStoragePercentage = storageProvider.calculateSystemPercentage()
    val internalStoragePercentage = storageProvider.calculateInternalPercentage()
    val batteryInfo = batteryDataProvider.getBatteryStatus()

    val cpuInfo = cpuDataProvider
        .getCpuCoresInformation()
        .map(CpuInfo::toDomainModel)
        .ifChanged()
        .mapToUiState(viewModelScope)

    val ramInfo = ramDataProvider
        .getRamInformation()
        .map(RamInfo::toDomainModel)
        .ifChanged()
        .mapToUiState(viewModelScope)

}

private fun <T> Flow<T>.logFlow(name: String = "Flow Logged"): Flow<T> {
    return onEach {
        Log.e(name, "data:$it")
    }
}
