package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

/**
 * 图表数据模型
 * 时间列表-timeList
 */
data class ChartListDataModel(val timeList: Array<String>?, val dataList: Array<ChartYDataList>?) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChartListDataModel

        if (!timeList.contentEquals(other.timeList)) return false
        if (!dataList.contentEquals(other.dataList)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timeList.contentHashCode()
        result = 31 * result + dataList.contentHashCode()
        return result
    }

    fun getXTimeList(): Array<String> {
        return timeList ?: arrayOf()
    }

    fun getYDataList(): Array<ChartYDataList> {
        return dataList ?: arrayOf()
    }
}

/**
 * Y轴数据模型
 */
data class ChartYDataList(val chartDataList: Array<Float>?, val type: String?) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChartYDataList

        if (!chartDataList.contentEquals(other.chartDataList)) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = chartDataList.contentHashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    fun getYDataList(): Array<Float> {
        return chartDataList ?: arrayOf()
    }

    /**
     * 获取具体折线或者柱状图小类型的类型名称
     */
    fun getTypeName(): String? {
        return when (type) {
            "pvout" -> MainApplication.instance().getString(R.string.photovoltaic_output)
            "oilout" -> MainApplication.instance().getString(R.string.oil_machine_output)
            "batCharge" -> MainApplication.instance().getString(R.string.battery_charge)
            "batDischarge" -> MainApplication.instance().getString(R.string.battery_discharge)
            "load" -> MainApplication.instance().getString(R.string.load_electricity)
            "toGrid" -> MainApplication.instance().getString(R.string.feed_into_the_grid)
            "fromGrid" -> MainApplication.instance().getString(R.string.take_electricity_grid)
            "ppv" -> MainApplication.instance().getString(R.string.pv_power)
            "ppv1" -> MainApplication.instance().getString(R.string.pv_power_1)
            "ppv2" -> MainApplication.instance().getString(R.string.pv_power_2)
            "ppv3" -> MainApplication.instance().getString(R.string.pv_power_3)
            "ppv4" -> MainApplication.instance().getString(R.string.pv_power_4)
            "ppv5" -> MainApplication.instance().getString(R.string.pv_power_5)
            "ipv1" -> MainApplication.instance().getString(R.string.pv_electricity_1)
            "ipv2" -> MainApplication.instance().getString(R.string.pv_electricity_2)
            "ipv3" -> MainApplication.instance().getString(R.string.pv_electricity_3)
            "ipv4" -> MainApplication.instance().getString(R.string.pv_electricity_4)
            "ipv5" -> MainApplication.instance().getString(R.string.pv_electricity_5)
            "vpv1" -> MainApplication.instance().getString(R.string.pv_voltage_1)
            "vpv2" -> MainApplication.instance().getString(R.string.pv_voltage_2)
            "vpv3" -> MainApplication.instance().getString(R.string.pv_voltage_3)
            "vpv4" -> MainApplication.instance().getString(R.string.pv_voltage_4)
            "vpv5" -> MainApplication.instance().getString(R.string.pv_voltage_5)
            else -> type
        }
    }
}
