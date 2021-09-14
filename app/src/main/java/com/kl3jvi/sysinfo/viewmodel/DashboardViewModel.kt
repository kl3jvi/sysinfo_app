package com.kl3jvi.sysinfo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kl3jvi.sysinfo.utils.MemoryExtended
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    val index = MutableLiveData<Long>()
    val ram = MemoryExtended(getApplication<Application>().applicationContext)


    fun repeatFun(): Job {
        return viewModelScope.launch {
            while (isActive) {
                //do your work here
                val percentage = toPercent(ram.getAvailableRam(), ram.totalRAM)
                index.value = 100 - percentage
                delay(timeMillis = TIME_MILLIS)
            }
        }
    }

    private fun toPercent(toCalculate: Long, maximum: Long): Long {
        return (toCalculate * 100L) / (maximum / 1048576L)
    }


    companion object {
        const val TIME_MILLIS = 1500L
    }
}