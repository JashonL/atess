package com.growatt.atess.model.mine

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

/**
 *消息中心
"id": 6005, //消息ID
"time": "2022-07-23 16:44", //故障时间
"title": "故障信息", // 标题
"faultContent": "序列号：NVCRBK107J(NVCRBK107J)的设备发生故障，请检查！", //故障信息
"plantName": "智能电表测试", //电站名称
"deviceType": "inv", //设备类型
"deviceSn": "NVCRBK107J", // 设备序列号
"isRead": 1 //是否已读  0 未读；1 已读
 */
data class MessageModel(
    val id: String,
    val time: String,
    val title: String,
    val plantName: String,
    val deviceSn: String,
    val deviceType: String,
    val faultContent: String,
    val isRead: Int
) {
    fun getPlantNameText(): String {
        return MainApplication.instance().getString(R.string.plant_name_format, plantName)
    }

    fun getDeviceSnText(): String {
        return MainApplication.instance().getString(R.string.sn_format, deviceSn)
    }

    fun getDeviceTypeText(): String {
        return MainApplication.instance().getString(R.string.device_type_format, deviceType)
    }
}

/**
 * 未读消息数量
 */
data class MessageUnReadNumResultModel(val unReadMsgNum: Int)
