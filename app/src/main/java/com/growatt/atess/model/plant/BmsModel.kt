package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

/**
 * BMS、MBMS、BCU_BMS模型
 */
data class BmsModel(
    val batFlag: Int, // 电池状态标志 -1 充电 0 无数据  1放电
    val soc: Int, // soc 70
    val vBat: Double,//电池总电压 484.5
    val cBat: Double,//电池总电流 16.5
    val maxTempStr: String,//最高温度
    val minTempStr: String,//最低温度
    val maxVoltStr: String,//单体最大电压
    val minVoltStr: String,//单体最小电压
) {

    companion object {
        /**
         * 创建图表类型(传给服务端的类型)
         * 1 —— soc
         */
        fun createChartType(): Array<ChartTypeModel> {
            return arrayOf(
                ChartTypeModel("1", MainApplication.instance().getString(R.string.current_soc), "%")
            )
        }
    }

    fun getSysStatusText(): String {
        return when (batFlag) {
            -1 -> MainApplication.instance().getString(R.string.charging)
            1 -> MainApplication.instance().getString(R.string.discharging)
            else -> ""
        }
    }

    fun getCurrentBatterySocText(): String {
        return MainApplication.instance().getString(R.string.current_battery_soc, soc) + "%"
    }

}