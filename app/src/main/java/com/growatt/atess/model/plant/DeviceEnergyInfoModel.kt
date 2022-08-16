package com.growatt.atess.model.plant

import androidx.annotation.DrawableRes
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

/**
 * 1 —— 光伏产出
 * 2 —— 油机产出
 * 3 —— 电池充电
 * 4 —— 电池放电
 * 5 —— 负载用电
 * 6 —— 馈入电网
 * 7 —— 电网取电
 * 8 —— 逆变器产出
 * 服务端逻辑-由于pcs没有统计光伏，所以当设备为pcs类型时，没有光伏数据
 */
data class DeviceEnergyInfoModel(
    val total: Double,
    val today: Double,
    val type: Int,
) {
    fun getTypeName(): String {
        return when (type) {
            1 -> MainApplication.instance().getString(R.string.photovoltaic_output)
            2 -> MainApplication.instance().getString(R.string.oil_machine_output)
            3 -> MainApplication.instance().getString(R.string.battery_charge)
            4 -> MainApplication.instance().getString(R.string.battery_discharge)
            5 -> MainApplication.instance().getString(R.string.load_electricity)
            6 -> MainApplication.instance().getString(R.string.feed_into_the_grid)
            7 -> MainApplication.instance().getString(R.string.take_electricity_grid)
            8 -> MainApplication.instance().getString(R.string.grid_connected_inverter_output)
            else -> ""
        }
    }

    @DrawableRes
    fun getTypeDrawableResId(): Int {
        return when (type) {
            1 -> R.drawable.ic_photovoltaic_output
            2 -> R.drawable.ic_oil_engine_output
            3 -> R.drawable.ic_battery_charge
            4 -> R.drawable.ic_battery_discharge
            5 -> R.drawable.ic_load_electricity
            6 -> R.drawable.ic_feed_into_the_grid
            7 -> R.drawable.ic_take_electricity_grid
            8 -> R.drawable.ic_grid_connected_inverter_output
            else -> R.drawable.ic_photovoltaic_output
        }
    }
}
