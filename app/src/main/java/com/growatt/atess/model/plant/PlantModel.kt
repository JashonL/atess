package com.growatt.atess.model.plant

import android.os.Parcelable
import com.growatt.lib.util.DateUtils
import com.growatt.lib.util.Util
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantModel(
    val id: String?,//电站ID
    val plantName: String?,//电站名
    val plantImgName: String?,//电站图片
    val createDateText: String?,//安装日期
    val hasDeviceOnLine: Int,//电站状态   0：离线  1：故障  2：在线
    val city: String?,//城市
    val timezone: String?,//时区
    val currentPacStr: String?,//实时功率0kW
    val nominalPowerStr: String?,//组件总功率1130kWp
    var nominalPower: String? = null,//组件总功率不带单位的，1130
    val eToday: Double?,//今日发电量
    val eTotal: Double?,//累计发电量
    val country: String?,//国家
    val plantAddress: String?,//详细地址
    var plant_lat: String? = null,//纬度
    var plant_lng: String? = null,//经度
    var formulaMoneyUnitId: String? = null,//货币单位
    var formulaMoney: String? = null//资金收益
) : Parcelable {

    companion object {
        /**
         * 类型-全部
         */
        const val PLANT_STATUS_ALL = -1

        /**
         * 类型-离线
         */
        const val PLANT_STATUS_OFFLINE = 0

        /**
         * 类型-故障
         */
        const val PLANT_STATUS_TROUBLE = 1

        /**
         * 类型-在线
         */
        const val PLANT_STATUS_ONLINE = 2

    }

    fun convert(): AddPlantModel {
        val addPlantModel = AddPlantModel()
        addPlantModel.plantID = id
        addPlantModel.plantName = plantName
        addPlantModel.plantFileService = plantImgName
        addPlantModel.installDate = DateUtils.from_yyyy_MM_dd_format(createDateText ?: "")
        addPlantModel.country = country
        addPlantModel.city = city
        addPlantModel.plantAddress = plantAddress
        addPlantModel.plantTimeZone = "+$timezone"
        addPlantModel.totalPower = nominalPower
        addPlantModel.formulaMoney = formulaMoney
        addPlantModel.formulaMoneyUnitId = formulaMoneyUnitId
        return addPlantModel
    }

    fun getETodayText(): String {
        return Util.getDoubleText(eToday)
    }

    fun getETotalText(): String {
        return Util.getDoubleText(eToday)
    }

}
