package com.kl3jvi.sysinfo.data.model

import com.kl3jvi.sysinfo.domain.models.CpuData

data class CpuInfo(
    val processorName: String,
    val abi: String,
    val coreNumber: Int,
    val hasArmNeon: Boolean,
    val frequencies: List<Frequency>,
    val l1dCaches: String,
    val l1iCaches: String,
    val l2Caches: String,
    val l3Caches: String,
    val l4Caches: String
) {
    data class Frequency(
        val min: Long,
        val max: Long,
        val current: Long
    )
}

fun CpuInfo.toDomainModel(): CpuData {
    val caches = listOf(l1dCaches, l1iCaches, l2Caches, l3Caches, l4Caches)
    return CpuData(
        processorName,
        abi,
        coreNumber,
        hasArmNeon,
        frequencies,
        caches
    )
}
