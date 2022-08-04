package com.growatt.atess.model.plant.ui

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.model.plant.DirectionType
import com.growatt.atess.util.ValueUtil
import com.growatt.lib.util.Util

/**
 * PCS系统运行图数据
 */
data class PcsSystemOperationModel(
    val pbdFlag: Int,//pv及pbd是否隐藏 0 隐藏 1 显示
    val ppv: Double,//光伏功率(PV功率)
    val pbdOutPower: Double, //pbd输出功率
    val runModel: String?,//运行模式
    val status: String?,//系统状态
    val bActivePower: Double,// 电网功率
    val gridFlag: Int,//电网流向 -1 电网取电， 0 无数据， 1 馈入电网
    val loadActivePower: Double,// 负载功率
    val oilMachineFlag: Int, //油机 0 隐藏 1 显示
    val dgGridPower: Double,//油机功率
    val soc: Double,// 电池电量百分比
    val pac: Double,// 电池功率
    val batFlag: Int,//电池流向 -1 充电， 0 无数据， 1 放电
    val atsBypass: Int,//Bypass 0 隐藏， 1 显示
    val gridInverterFlag: Int//并网逆变器 0 隐藏 1 显示
) {

    fun isShowPbd(): Boolean {
        return pbdFlag == 1
    }

    /**
     * PBD设备流向
     */
    @DirectionType
    fun getPbdDirection(): Int {
        if (isShowPbd()) {
            return DirectionType.OUTPUT
        }
        return DirectionType.HIDE
    }

    fun isShowPV(): Boolean {
        return isShowPbd()
    }

    /**
     * PV光伏设备流向
     */
    @DirectionType
    fun getPVDirection(): Int {
        if (isShowPV()) {
            return DirectionType.OUTPUT
        }
        return DirectionType.HIDE
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
     * PCS设备流向
     */
    @DirectionType
    fun getPcsDirection(): Int {
        return when (getBatteryDirection()) {
            DirectionType.INPUT -> DirectionType.OUTPUT
            DirectionType.OUTPUT -> DirectionType.INPUT
            else -> return when (getPbdDirection()) {
                DirectionType.OUTPUT -> DirectionType.INPUT
                else -> DirectionType.HIDE
            }
        }
    }

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

    fun isShowOilEngine(): Boolean {
        return oilMachineFlag == 1
    }

    /**
     * 油机流向
     * 流向的显示隐藏判断逻辑：功率为0时隐藏，不为0则显示
     */
    @DirectionType
    fun getOilEngineDirection(): Int {
        if (dgGridPower == 0.0) {
            return DirectionType.HIDE
        }
        return DirectionType.OUTPUT
    }

    fun isShowBypass(): Boolean {
        return atsBypass == 1
    }

    /**
     * ByPass流向
     */
    @DirectionType
    fun getByPassDirection(): Int {
        return getPcsDirection()
    }

    fun isShowInverter(): Boolean {
        return gridInverterFlag == 1
    }

    /**
     * 并网逆变器流向
     */
    @DirectionType
    fun getInverterDirection(): Int {
        if (isShowInverter()) {
            return DirectionType.OUTPUT
        }
        return DirectionType.HIDE
    }

    /**
     * 右下角往左方向图标流向
     */
    @DirectionType
    fun getLeftDirection(): Int {
        if (isShowInverter() && isShowOilEngine()) {
            return DirectionType.OUTPUT
        }
        return DirectionType.HIDE
    }

    /**
     * 负载流向
     * 流向的显示隐藏判断逻辑：功率为0时隐藏，不为0则显示
     */
    @DirectionType
    fun getLoadDirection(): Int {
        if (loadActivePower == 0.0) {
            return DirectionType.HIDE
        }
        return DirectionType.INPUT
    }

    fun getPbdText(): String {
        val valueFromW = ValueUtil.valueFromW(pbdOutPower)
        return MainApplication.instance()
            .getString(R.string.pbd_output_power_format, valueFromW.first + valueFromW.second)
    }

    fun getPVText(): String {
        val valueFromW = ValueUtil.valueFromW(ppv)
        return MainApplication.instance()
            .getString(R.string.pv_power_format, valueFromW.first + valueFromW.second)
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

    fun getOilEngineText(): String {
        val valueFromW = ValueUtil.valueFromW(dgGridPower)
        return MainApplication.instance()
            .getString(R.string.oil_engine_power_format, valueFromW.first + valueFromW.second)
    }

    fun getLoadText(): String {
        val valueFromW = ValueUtil.valueFromW(loadActivePower)
        return MainApplication.instance()
            .getString(R.string.load_power_format, valueFromW.first + valueFromW.second)
    }

    fun getGridText(): String {
        val valueFromW = ValueUtil.valueFromW(bActivePower)
        return MainApplication.instance()
            .getString(R.string.grid_power_format, valueFromW.first + valueFromW.second)
    }
}
