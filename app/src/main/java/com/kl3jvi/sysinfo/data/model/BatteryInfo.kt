package com.kl3jvi.sysinfo.data.model

data class BatteryInfo(
    val level: String,
    val health: String,
    val voltage: String,
    val temperature: String,
    val capacity: String,
    val technology: String,
    val isCharging: Boolean,
    val chargingType: String
)
