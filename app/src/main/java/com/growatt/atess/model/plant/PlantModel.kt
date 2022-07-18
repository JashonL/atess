package com.growatt.atess.model.plant

import android.os.Parcelable
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
    val currentPacStr: String?,//实时功率0kW
    val nominalPowerStr: String?,//组件总功率1130kWp
    val eToday: Double?,//今日发电量
    val eTotal: Double?//累计发电量
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

    fun getETodayText(): String {
        return Util.getDoubleText(eToday)
    }

    fun getETotalText(): String {
        return Util.getDoubleText(eToday)
    }

}

