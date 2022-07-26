package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

/**
 * 电站详情服务端返回来Model
 */
data class PlantInfoResultModel(val plantBean: PlantModel, val weatherMap: WeatherModel)


data class WeatherModel(
    val success: Boolean,//天气是否获取成功
    val icon: String?,//天气图标
    val newTmp: String?,//温度-32.0°C
    val city: String?,//城市
    val cond_txt: String?,//晴
    val wind_dir: String?,//风向-北风
    val wind_spd: String?,// 风速-20km/h
    val sr: String?,//日出-06:03
    val ss: String?,//日落-18:10
    val duration: String?//日照时长-12h07min
) {
    fun getCityAndWeatherText(): String {
        return "$city | $cond_txt"
    }

    fun getWindDirectionText(): String {
        return MainApplication.instance().getString(R.string.wind_direction_format, wind_dir)
    }

    fun getWindSpeedText(): String {
        return MainApplication.instance().getString(R.string.wind_speed_format, wind_spd)
    }
}
