package com.kl3jvi.sysinfo.data.model

import com.kl3jvi.sysinfo.domain.models.CpuData
import com.kl3jvi.sysinfo.utils.clearEmptyEntries
import com.kl3jvi.sysinfo.utils.toAffirmative
import com.kl3jvi.sysinfo.utils.toInformation

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
    val info = listOf(
        "Processor Name" to processorName,
        "ABI" to abi,
        "Core Number" to coreNumber.toString(),
        "Arm Neon" to hasArmNeon.toAffirmative(),
        "L1d Cache" to l1dCaches,
        "L1i Cache" to l1iCaches,
        "L2 Cache" to l2Caches,
        "L3 Cache" to l3Caches,
        "L4 Cache" to l4Caches
    ).map(Pair<String, String>::toInformation)
        .clearEmptyEntries()

    return CpuData(
        processorName,
        abi,
        coreNumber,
        hasArmNeon,
        frequencies,
        info
    )
}
