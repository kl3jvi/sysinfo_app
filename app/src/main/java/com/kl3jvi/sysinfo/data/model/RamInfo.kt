package com.kl3jvi.sysinfo.data.model

import com.kl3jvi.sysinfo.domain.models.RamData
import com.kl3jvi.sysinfo.utils.humanReadableByteCount

data class RamInfo(
    val total: Long = 0L,
    val available: Long = 0L,
    val percentageAvailable: Int = 0,
    val threshold: Long = 0L
)


fun RamInfo.toHumanReadableValues(): RamData {
    return RamData(
        humanReadableByteCount(total),
        humanReadableByteCount(available),
        percentageAvailable,
        humanReadableByteCount(threshold)
    )
}


