package com.growatt.atess.model.plant

/**
 * 总览头部信息
 */
data class SynopsisTotalModel(
    val xxx_2: Double,//今日发电
    val soc: Int,//剩余电量的百分比，80%的话这个值就是80
    val loadToday: Double,//负载用电-今日用电
    val xxx_1: Double,//负载用电-单月用电
    val loadTotal: Double,//负载用电-累计用电
    val toGrid: Double,//电网用电-馈入电网
    val fromGrid: Double,//电网用电-电网取电
    val gridTotal: Double,//电网用电-电网总耗电量
    val xxx_3: Double,//组件总功率kWp
    val plantNum: Int,//电站数量
    val alarmValue: Int,//告警信息
    val faultDeviceNum: Int,//故障设备数量
    val offlineDeviceNum: Int,//离线设备数量
    val onlineDeviceNum: Int,//在线设备数量
)
