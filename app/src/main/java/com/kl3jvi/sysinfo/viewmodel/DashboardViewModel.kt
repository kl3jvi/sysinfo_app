package com.kl3jvi.sysinfo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kl3jvi.sysinfo.utils.DeviceInfoExtended
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val deviceInfoExtended =
        DeviceInfoExtended(getApplication<Application>().applicationContext)

    val ram = MutableLiveData<Long>()
    val ramText = MutableLiveData<String>()
    val frequencies = MutableLiveData<ArrayList<Long>>()

    fun repeatFun(): Job {
        return viewModelScope.launch {
            while (isActive) {
                //do your work here
                ramUi()

                // Time Delay
                delay(timeMillis = TIME_MILLIS)
            }
        }
    }

    private fun ramUi() {
        val percentage =
            toPercent(
                deviceInfoExtended.getAvailableRam(),
                deviceInfoExtended.totalDeviceRam()
            )
        ram.value = percentage
        val usedRam =
            deviceInfoExtended.totalDeviceRam() - deviceInfoExtended.getAvailableRam()
        ramText.value =
            "$usedRam / ${deviceInfoExtended.totalDeviceRam()} MB"
    }


    private fun toPercent(toCalculate: Long, maximum: Long): Long {
        return (100 - (toCalculate * 100L) / maximum)
    }


    companion object {
        const val TIME_MILLIS = 1500L
    }
}