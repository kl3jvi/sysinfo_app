package com.kl3jvi.sysinfo.data.model

import com.kl3jvi.sysinfo.domain.models.RamData
import com.kl3jvi.sysinfo.utils.humanReadableByteCount
import java.io.Serializable

data class RamInfo(
    val total: Long = 0L,
    val available: Long = 0L,
    val percentageAvailable: Int = 0,
    val threshold: Long = 0L,
    val ramLoad: RamLoad
) : Serializable

fun RamInfo.toDomainModel(): RamData {
    return RamData(
        total = humanReadableByteCount(total),
        available = humanReadableByteCount(available),
        percentageAvailable = percentageAvailable,
        threshold = humanReadableByteCount(threshold),
        ramLoad = ramLoad
    )
}
