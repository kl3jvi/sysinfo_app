package com.kl3jvi.sysinfo.domain.models

import com.kl3jvi.sysinfo.data.model.CpuInfo

data class CpuData(
    val processorName: String,
    val abi: String,
    val coreNumber: Int,
    val hasArmNeon: Boolean,
    val frequencies: List<CpuInfo.Frequency>,
    val layoutInfo: List<Information>
)
