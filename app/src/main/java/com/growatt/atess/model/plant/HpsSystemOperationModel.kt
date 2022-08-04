package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.util.ValueUtil
import com.growatt.lib.util.Util

/**
 * HPS系统运行图数据
 * 1.油机与电网只可能一个有数据，或者都没有数据
 * 2.油机没数据就不展示
 */
data class HpsSystemOperationModel(
    val ppv: Double,//光伏功率(PV功率)
    val runModel: String?,//运行模式
    val status: String?,//系统状态
    val soc: Double,// 电池电量百分比
    val pac: Double,// 电池功率
    val batFlag: Int,//电池流向 -1 充电， 0 无数据， 1 放电
    val loadActivePower: Double?,// 负载功率
    val atsBypass: Int,//ATS 0 隐藏， 1 显示
    val dgGridPower: Double,//油机功率
    val oilMachineEnable: Int, //油机 0 隐藏 1 显示
    val bActivePower: Double,// 电网功率
    val gridFlag: Int,//电网流向 -1 电网取电， 0 无数据， 1 馈入电网
) {

    /**
     * 电网流向
     */
    @DirectionType
    fun getGridDirection(): Int {
        return when (gridFlag) {
            -1 -> DirectionType.OUTPUT
            1 -> DirectionType.INPUT
            else -> DirectionType.HIDE
        }
    }

    /**
     * 油机流向
     */
    @DirectionType
    fun getOilEngineDirection(): Int {
        if (isShowOilEngine()) {
            return DirectionType.OUTPUT
        }
        return DirectionType.HIDE
    }

    /**
     * ATS流向,油机显示时与油机流向(流出)一致，电网显示时与电网流向一致
     */
    @DirectionType
    fun getAtsDirection(): Int {
        return if (isShowOilEngine()) getOilEngineDirection() else getGridDirection()
    }

    /**
     * PV光伏流向
     */
    @DirectionType
    fun getPVDirection(): Int {
        if (ppv == 0.0) {
            return DirectionType.HIDE
        }
        return DirectionType.OUTPUT
    }

    /**
     * 负载流向
     */
    @DirectionType
    fun getLoadDirection(): Int {
        if (loadActivePower == 0.0) {
            return DirectionType.HIDE
        }
        return DirectionType.OUTPUT
    }

    /**
     * 电池流向
     */
    @DirectionType
    fun getBatteryDirection(): Int {
        return when (batFlag) {
            -1 -> DirectionType.INPUT
            1 -> DirectionType.OUTPUT
            else -> DirectionType.HIDE
        }
    }

    /**
     * 是否显示ATS
     */
    fun isShowATS(): Boolean {
        return atsBypass == 1
    }

    /**
     * 是否显示油机
     */
    fun isShowOilEngine(): Boolean {
        return oilMachineEnable == 1
    }

    fun getOilEngineText(): String {
        val valueFromW = ValueUtil.valueFromW(dgGridPower)
        return MainApplication.instance()
            .getString(R.string.oil_engine_power_format, valueFromW.first + valueFromW.second)
    }

    fun getPVText(): String {
        val valueFromW = ValueUtil.valueFromW(ppv)
        return MainApplication.instance()
            .getString(R.string.pv_power_format, valueFromW.first + valueFromW.second)
    }

    fun getGridText(): String {
        val valueFromW = ValueUtil.valueFromW(bActivePower)
        return MainApplication.instance()
            .getString(R.string.grid_power_format, valueFromW.first + valueFromW.second)
    }

    fun getLoadText(): String {
        val valueFromW = ValueUtil.valueFromW(loadActivePower)
        return MainApplication.instance()
            .getString(R.string.load_power_format, valueFromW.first + valueFromW.second)
    }

    fun getChargeText(): String {
        val valueFromW = ValueUtil.valueFromW(pac)
        return MainApplication.instance()
            .getString(R.string.charge_power_format, valueFromW.first + valueFromW.second)
    }

    fun getBatteryPercentText(): String {
        return MainApplication.instance()
            .getString(R.string.battery_percent_format, Util.getDoubleText(soc) + "%")
    }
}
