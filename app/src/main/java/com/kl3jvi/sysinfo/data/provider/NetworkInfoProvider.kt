package com.kl3jvi.sysinfo.data.provider

import android.net.wifi.WifiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.net.InetAddress
import kotlin.math.pow


class NetworkInfoProvider(
    private val wifiManager: WifiManager
) {

    private fun getLocalIPAndSubnetMask(): Pair<InetAddress, Int>? {
        val dhcpInfo = wifiManager.dhcpInfo ?: return null

        val ipAddress = InetAddress.getByAddress(dhcpInfo.ipAddress.toByteArray())
        val subnetMask = Integer.bitCount(dhcpInfo.netmask)

        return Pair(ipAddress, subnetMask)
    }

    suspend fun scanNetwork(): List<String> = coroutineScope {
        val (baseIP, mask) = getLocalIPAndSubnetMask()
            ?: return@coroutineScope emptyList<String>()
        val totalHosts = 2.0.pow(32 - mask).toInt()
        val batchSize = 256  // Number of IPs to ping concurrently

        val baseParts = baseIP.hostAddress ?: return@coroutineScope emptyList()
        val splittedBaseParts = baseParts.split(".").map { it.toInt() }

        val activeDevices = mutableListOf<String>()

        for (batchStart in 0 until totalHosts step batchSize) {
            val jobs = (batchStart until (batchStart + batchSize).coerceAtMost(totalHosts)).map { offset ->
                async(Dispatchers.IO) {
                    val ipParts = splittedBaseParts.toMutableList()
                    var carry = offset
                    for (i in 3 downTo 0) {
                        ipParts[i] += carry
                        carry = if (ipParts[i] > 255) ipParts[i] / 256 else 0
                        ipParts[i] %= 256
                    }
                    val ip = ipParts.joinToString(".")
                    if (ping(ip)) {
                        synchronized(activeDevices) {
                            activeDevices.add(ip)
                        }
                    }
                }
            }
            jobs.forEach { it.await() }
        }

        activeDevices
    }

    private fun ping(ip: String): Boolean {
        return try {
            val address = InetAddress.getByName(ip)
            address.isReachable(1000)
        } catch (e: Exception) {
            false
        }
    }

    private fun Int.toByteArray(): ByteArray {
        return byteArrayOf(
            (this shr 24).toByte(),
            (this shr 16).toByte(),
            (this shr 8).toByte(),
            this.toByte()
        )
    }
}