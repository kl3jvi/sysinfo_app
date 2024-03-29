package com.kl3jvi.sysinfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.sysinfo.data.model.BatteryInfo
import com.kl3jvi.sysinfo.data.model.CpuInfo
import com.kl3jvi.sysinfo.data.model.RamInfo
import com.kl3jvi.sysinfo.data.model.toDomainModel
import com.kl3jvi.sysinfo.data.provider.BatteryDataProvider
import com.kl3jvi.sysinfo.data.provider.CpuDataProvider
import com.kl3jvi.sysinfo.data.provider.DeviceDataProvider
import com.kl3jvi.sysinfo.data.provider.GpuDataProvider
import com.kl3jvi.sysinfo.data.provider.NetworkInfoProvider
import com.kl3jvi.sysinfo.data.provider.RamDataProvider
import com.kl3jvi.sysinfo.data.provider.StorageProvider
import com.kl3jvi.sysinfo.data.provider.SystemInfoProvider
import com.kl3jvi.sysinfo.utils.asResult
import com.kl3jvi.sysinfo.utils.ifChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DataViewModel(
    cpuDataProvider: CpuDataProvider,
    ramDataProvider: RamDataProvider,
    storageProvider: StorageProvider,
    batteryDataProvider: BatteryDataProvider,
    gpuDataProvider: GpuDataProvider,
    deviceDataProvider: DeviceDataProvider,
    systemInfoProvider: SystemInfoProvider,
    networkInfoProvider: NetworkInfoProvider
) : ViewModel() {

    val systemStoragePercentage = storageProvider.calculateSystemPercentage()
    val internalStoragePercentage = storageProvider.calculateInternalPercentage()
    val externalStoragePercentage = storageProvider.calculateExternalPercentage()

    val batteryInfo =
        batteryDataProvider.getBatteryStatus().map(BatteryInfo::toDomainModel).ifChanged()

    val cpuInfo =
        cpuDataProvider.getCpuCoresInformation().map(CpuInfo::toDomainModel).ifChanged().asResult()

    val ramInfo =
        ramDataProvider.getRamInformation().map(RamInfo::toDomainModel).ifChanged().asResult()

    val systemInfo =
        systemInfoProvider.getSystemInfo()
    val uptimeFlow = systemInfoProvider.getSystemUptimeFlow()

    val deviceData = deviceDataProvider.getDeviceInformation()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            networkInfoProvider.scanNetwork().forEach {
                Log.e("c", it)
            }
        }
    }
}

private fun <T> Flow<T>.logFlow(name: String = "Flow Logged"): Flow<T> {
    return onEach {
        Log.e(name, "data:$it")
    }
}
