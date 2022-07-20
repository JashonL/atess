package com.growatt.atess.model.plant

import androidx.annotation.IntDef

/**
 * 设备状态
 */
@IntDef(
    DeviceType.HPS,
    DeviceType.PCS,
    DeviceType.PBD,
    DeviceType.BMS,
    DeviceType.MBMS,
    DeviceType.COMBINER,
    DeviceType.COLLECTOR
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class DeviceType {
    companion object {
        const val HPS = 1
        const val PCS = 2
        const val PBD = 3
        const val BMS = 4
        const val MBMS = 5
        const val COMBINER = 6
        const val COLLECTOR = 0
    }
}