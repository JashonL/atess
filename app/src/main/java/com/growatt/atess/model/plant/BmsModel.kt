package com.growatt.atess.model.plant

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.lib.util.Util
import kotlinx.parcelize.Parcelize

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
    val batteryVO: BatteryModel?//电池数据
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

@Parcelize
data class BatteryModel(
    val batName: String?,//电池名称
    val typeName: String?,//类型名称
    val type: String?,//类型 0 电池组， 1 电池串， 2电池箱， 3 电芯
    val maxVol: Double?,//最大电压
    val minVol: Double?,//最小电压
    val vol: Double?,//电压
    val childList: Array<BatteryModel>?
) : Parcelable {

    @DrawableRes
    fun getIcon(): Int {
        return when (type) {
            "1" -> R.drawable.ic_battery_string
            "2" -> R.drawable.ic_battery_box
            "3" -> R.drawable.ic_batteries
            else -> R.drawable.ic_battery_box
        }
    }

    fun showMaxVol(): Boolean {
        return maxVol != null
    }

    fun showMinVol(): Boolean {
        return minVol != null
    }

    fun showVol(): Boolean {
        return vol != null
    }

    fun getMaxVolText(): String {
        return MainApplication.instance()
            .getString(
                R.string.max_voltage_format,
                Util.getDoubleText(maxVol) + MainApplication.instance().getString(R.string.v)
            )
    }

    fun getMinVolText(): String {
        return MainApplication.instance()
            .getString(
                R.string.min_voltage_format,
                Util.getDoubleText(minVol) + MainApplication.instance().getString(R.string.v)
            )
    }

    fun getVolText(): String {
        return MainApplication.instance()
            .getString(
                R.string.voltage_format,
                Util.getDoubleText(vol) + MainApplication.instance().getString(R.string.v)
            )
    }

    private fun getSafeChildList(): Array<BatteryModel> {
        return childList ?: arrayOf()
    }

    /**
     * 获取最多不超过2个长度的List
     */
    fun getBatteryListNoMoreThanTwo(): List<BatteryModel> {
        val batteryList = mutableListOf<BatteryModel>()
        for (child in getSafeChildList()) {
            batteryList.add(child)
        }
        return batteryList
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BatteryModel

        if (batName != other.batName) return false
        if (typeName != other.typeName) return false
        if (type != other.type) return false
        if (maxVol != other.maxVol) return false
        if (minVol != other.minVol) return false
        if (vol != other.vol) return false
        if (childList != null) {
            if (other.childList == null) return false
            if (!childList.contentEquals(other.childList)) return false
        } else if (other.childList != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = batName?.hashCode() ?: 0
        result = 31 * result + (typeName?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (maxVol?.hashCode() ?: 0)
        result = 31 * result + (minVol?.hashCode() ?: 0)
        result = 31 * result + (vol?.hashCode() ?: 0)
        result = 31 * result + (childList?.contentHashCode() ?: 0)
        return result
    }

}