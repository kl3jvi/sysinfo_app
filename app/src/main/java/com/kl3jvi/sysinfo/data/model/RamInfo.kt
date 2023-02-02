package com.kl3jvi.sysinfo.data.model

data class RamInfo(
    val total: Long,
    val available: Long,
    val percentageAvailable: Int,
    val threshold: Long,
)