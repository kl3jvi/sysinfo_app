package com.kl3jvi.sysinfo.utils

import kotlin.math.ln
import kotlin.math.pow

/**
 * Formats the given number of bytes into a human-readable string with the size and unit, such as "4.00 MB".
 *
 * @param bytes The number of bytes.
 *
 * @return A human-readable string representation of the given number of bytes.
 */
fun humanReadableByteCount(bytes: Long): String {
    val units = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB")
    val unitIndex = (ln(bytes.toDouble()) / ln(1024.0)).toInt()
    return "%.2f %s".format(bytes / 1024.0.pow(unitIndex.toDouble()), units[unitIndex])
}

/**
 * Returns a [String] representation of the cache size, in a human readable format, of the [IntArray].
 * @return [String] representation of the cache size in a human readable format. An empty string if the [IntArray] is null.
 */
fun IntArray?.cacheHumanReadable(): String {
    return this?.map { it.toLong() }?.joinToString(
        separator = "\n",
        transform = ::humanReadableByteCount
    ).orEmpty()
}
