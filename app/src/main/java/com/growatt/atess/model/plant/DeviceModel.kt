package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.util.ValueUtil
import com.growatt.lib.util.Util

/**
 * 设备列表Model
 */
data class DeviceModel(
    val deviceType: String?,//设备类型
    val deviceModel: String?,//设备型号
    val deviceSn: String?,//设备SN
    val soc: Int?,//剩余电量
    val sysStatus: Int?,// 系统状态 -1充电 0 无数据 +1 放电
    val eToday: Double?,//今日发电量
    val eTotal: Double?,//累计发电量
    val lost: Boolean?,//1.(true-离线、false-在线),2.(true-未连接、false-已连接)
    val interval: String?,//采集器-更新间隔，"0"
    val lastUpdate: String?,//采集器-最后更新时间，"2020-06-01 07:00:48"
    val power: Double?,//Combiner汇流箱-功率1.0
    val vol: Double?,//Combiner汇流箱-电压1.0
    val cur: Double?,//Combiner汇流箱-电流2.0
) {
    fun getETodayText(): String {
        val valueFromKWh = ValueUtil.valueFromKWh(eToday)
        return MainApplication.instance()
            .getString(
                R.string.today_generate_electricity_format,
                valueFromKWh.first + valueFromKWh.second
            )
    }

    fun getETotalText(): String {
        val valueFromKWh = ValueUtil.valueFromKWh(eTotal)
        return MainApplication.instance()
            .getString(
                R.string.total_generate_electricity_format,
                valueFromKWh.first + valueFromKWh.second
            )
    }

    fun getRealDeviceType(): Int {
        return when {
            "hps" == deviceType -> DeviceType.HPS
            "pbd" == deviceType -> DeviceType.PBD
            "pcs" == deviceType -> DeviceType.PCS
            "bms" == deviceType -> DeviceType.BMS
            "mbms" == deviceType -> DeviceType.MBMS
            "bcu_bms" == deviceType -> DeviceType.BCU_BMS
            "combiner" == deviceType -> DeviceType.COMBINER
            else -> DeviceType.COLLECTOR
        }
    }

    fun getDeviceSN(): String? {
        return deviceSn
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
            .getString(if (lost == true) R.string.disconnect else R.string.connected)
        return MainApplication.instance()
            .getString(R.string.connect_status_format, connectStatusText)
    }

    fun getSocText(): String {
        return MainApplication.instance().getString(R.string.soc_format, soc) + "%"
    }

    fun getStatusText(): String {
        return MainApplication.instance()
            .getString(if (lost == true) R.string.offline else R.string.online)
    }

    fun getConnectStatusText2(): String {
        return MainApplication.instance()
            .getString(if (lost == true) R.string.disconnect else R.string.connected)
    }

    fun getUpdateIntervalText(): String {
        return MainApplication.instance()
            .getString(R.string.update_interval_format, interval)
    }

    fun getLastUpdateTimeText(): String {
        return MainApplication.instance()
            .getString(R.string.last_update_time_format, lastUpdate)
    }

    /**
     * 功率
     */
    fun getPowerText(): String {
        val valueFromW = ValueUtil.valueFromW(power)
        return MainApplication.instance()
            .getString(R.string.current_power_format, valueFromW.first + valueFromW.second)
    }

    /**
     * 电流
     */
    fun getElectricityText(): String {
        return MainApplication.instance()
            .getString(
                R.string.electricity_format,
                Util.getDoubleText(cur) + MainApplication.instance().getString(R.string.a)
            )
    }

    /**
     * 电压
     */
    fun getVoltageText(): String {
        return MainApplication.instance()
            .getString(
                R.string.voltage_format,
                Util.getDoubleText(vol) + MainApplication.instance().getString(R.string.v)
            )
    }
}
