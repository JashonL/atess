package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.lib.util.Util

/**
 * 设备列表Model
 */
data class DeviceModel(
    val deviceType: String?,//设备类型
    val deviceModel: String?,//设备型号
    val hpsid: String?,//HPS设备id
    val pbdid: String?,//PBD设备id
    val pcsid: String?,//PCS设备id
    val bmsid: String?,//BMS设备id
    val datalogID: String?,//采集器设备id
    val soc: Int?,//剩余电量
    val sysStatus: Int?,// 系统状态 -1充电 0 无数据 +1 放电
    val eToday: Double?,//今日发电量
    val eTotal: Double?,//累计发电量
    val lost: Boolean,//1.(true-在线、false-离线),2.(true-已连接、false-未连接)
    val interval: String,//采集器-更新间隔，"0"
    val lastUpdate: String//采集器-最后更新时间，"2020-06-01 07:00:48"
) {
    fun getETodayText(): String {
        return MainApplication.instance()
            .getString(
                R.string.today_power_format,
                "${Util.getDoubleText(eToday)}${MainApplication.instance().getString(R.string.kwh)}"
            )
    }

    fun getETotalText(): String {
        return MainApplication.instance()
            .getString(
                R.string.total_power_format,
                "${Util.getDoubleText(eToday)}${MainApplication.instance().getString(R.string.kwh)}"
            )
    }

    fun getRealDeviceType(): Int {
        return when {
            !hpsid.isNullOrEmpty() -> DeviceType.HPS
            !pbdid.isNullOrEmpty() -> DeviceType.PBD
            !pcsid.isNullOrEmpty() -> DeviceType.PCS
            !bmsid.isNullOrEmpty() && "bms" == deviceType -> DeviceType.BMS
            !bmsid.isNullOrEmpty() && "mbms" == deviceType -> DeviceType.MBMS
            else -> DeviceType.COLLECTOR
        }
    }

    fun getSysStatusText(): String {
        return when (sysStatus) {
            -1 -> MainApplication.instance().getString(R.string.charging)
            1 -> MainApplication.instance().getString(R.string.discharging)
            else -> ""
        }
    }

    fun getConnectStatusText(): String {
        val connectStatusText = MainApplication.instance()
            .getString(if (lost) R.string.connected else R.string.disconnect)
        return MainApplication.instance()
            .getString(R.string.connect_status_format, connectStatusText)
    }

    fun getSocText(): String {
        return MainApplication.instance().getString(R.string.soc_format, soc) + "%"
    }

    fun getStatusText(): String {
        return MainApplication.instance().getString(if (lost) R.string.online else R.string.offline)
    }

    fun getConnectStatusText2(): String {
        return MainApplication.instance()
            .getString(if (lost) R.string.connected else R.string.disconnect)
    }

    fun getUpdateIntervalText(): String {
        return MainApplication.instance()
            .getString(R.string.update_interval_format, interval)
    }

    fun getLastUpdateTimeText(): String {
        return MainApplication.instance()
            .getString(R.string.last_update_time_format, lastUpdate)
    }
}
