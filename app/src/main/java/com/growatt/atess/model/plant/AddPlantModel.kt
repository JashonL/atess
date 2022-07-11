package com.growatt.atess.model.plant

import com.growatt.lib.util.DateUtils
import java.util.*

/**
 * 添加电站
 */
class AddPlantModel {
    //电站名
    var plantName: String? = null

    //安装日期
    var installDate: Date? = null

    //国家
    var country: String? = null

    //城市
    var city: String? = null

    //详细地址
    var plantAddress: String? = null

    //纬度
    var plant_lat: Double? = null

    //经度
    var plant_lng: Double? = null

    //总功率
    var nominalPower: String? = null

    //时区
    var plantTimeZone: String? = null

    //收益公式
    var formulaMoney: String? = null

    //货币单位
    var formulaMoneyUnitId: String? = null

    //电站图片
    var plantFile: String? = null


    fun getDateString(): String {
        if (installDate == null) {
            installDate = Date()
        }
        return DateUtils.yyyy_MM_dd_format(installDate!!)
    }

}