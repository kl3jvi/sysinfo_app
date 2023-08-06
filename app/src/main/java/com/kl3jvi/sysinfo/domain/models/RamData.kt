package com.kl3jvi.sysinfo.domain.models

import com.kl3jvi.sysinfo.data.provider.RamLoad

data class RamData(
    val total: String,
    val available: String,
    val percentageAvailable: Int,
    val threshold: String,
    val ramLoad: RamLoad
)
