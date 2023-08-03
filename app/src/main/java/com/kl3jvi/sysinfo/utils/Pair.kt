package com.kl3jvi.sysinfo.utils

import com.kl3jvi.sysinfo.domain.models.Information

fun Pair<String, String>.toInformation(): Information {
    return Information(
        title = first,
        details = second
    )
}