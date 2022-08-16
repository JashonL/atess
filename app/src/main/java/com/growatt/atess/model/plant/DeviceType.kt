package com.growatt.atess.model.plant

import androidx.annotation.IntDef
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

/**
 * 设备状态
 */
@IntDef(
    DeviceType.HPS,
    DeviceType.PCS,
    DeviceType.PBD,
    DeviceType.BMS,
    DeviceType.MBMS,
    DeviceType.BCU_BMS,
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
        const val BCU_BMS = 6
        const val COMBINER = 7
        const val COLLECTOR = 0

        fun getDeviceTypeName(@DeviceType deviceType: Int): String {
            return MainApplication.instance().getString(
                when (deviceType) {
                    HPS -> R.string.hps
                    PCS -> R.string.pcs
                    PBD -> R.string.pbd
                    BMS -> R.string.bms
                    MBMS -> R.string.mbms
                    BCU_BMS -> R.string.bcu_bms
                    COMBINER -> R.string.combiner
                    else -> R.string.collector
                }
            )
        }
    }
}