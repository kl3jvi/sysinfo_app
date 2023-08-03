package com.kl3jvi.sysinfo.data.model

import com.kl3jvi.sysinfo.data.model.BatteryType.Companion.asString
import com.kl3jvi.sysinfo.domain.models.BatteryData
import com.kl3jvi.sysinfo.utils.toInformation
import java.util.Locale

data class BatteryInfo(
    val level: String,
    val health: String,
    val voltage: String,
    val temperature: String,
    val capacity: String,
    val technology: String,
    val isCharging: BatteryType,
    val chargingType: String
)

enum class BatteryType {
    CHARGING,
    FULL,
    DISCHARGING,
    NOT_CHARGING,
    UNKNOWN;

    companion object {

        fun BatteryType.asString(): String {
            return this.name.split('_').joinToString(" ") {
                it.lowercase(Locale.getDefault())
                    .replaceFirstChar { char -> char.titlecase(Locale.getDefault()) }
            }
        }
    }
}

fun BatteryInfo.toDomainModel(): BatteryData {
    val listOfInfo = listOf(
        "Level" to level,
        "Health" to health,
        "Voltage" to voltage,
        "Temperature" to temperature,
        "Capacity" to capacity,
        "Technology" to technology,
        "Status" to isCharging.asString(),
        "Power Source" to chargingType,
    ).map(Pair<String, String>::toInformation)

    return BatteryData(listOfInfo)
}