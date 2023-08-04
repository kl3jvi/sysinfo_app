package com.kl3jvi.sysinfo.domain.models

data class RamData(
    val total: String,
    val available: String,
    val percentageAvailable: Int,
    val threshold: String
)
