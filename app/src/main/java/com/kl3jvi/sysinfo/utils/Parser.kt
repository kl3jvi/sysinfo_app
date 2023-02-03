package com.kl3jvi.sysinfo.utils

fun humanReadableByteCount(bytes: Long): String {
    val units = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB")
    val unitIndex = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
    return "%.2f %s".format(bytes / Math.pow(1024.0, unitIndex.toDouble()), units[unitIndex])
}

fun IntArray?.cacheHumanReadable(): String {
    return this?.map { it.toLong() }?.joinToString(
        separator = "\n",
        transform = ::humanReadableByteCount
    ).orEmpty()
}