package com.kl3jvi.sysinfo.data.model

import kotlinx.coroutines.flow.Flow
import java.util.Locale

data class BatteryInfo(
    val level: String,
    val health: String,
    val voltage: String,
    val temperature: String,
    val capacity: String,
    val technology: String,
    val isCharging: Flow<BatteryType>,
    val chargingType: String
)

enum class BatteryType {
    CHARGING,
    FULL,
    DISCHARGING,
    NOT_CHARGING,
    UNKNOWN;

    companion object {

        fun BatteryType.asString(): String? {
            return this.name.split('_').joinToString(" ") {
                it.lowercase(Locale.getDefault())
                    .replaceFirstChar { char -> char.titlecase(Locale.getDefault()) }
            }
        }
    }
}