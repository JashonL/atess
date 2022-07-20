package com.growatt.atess.model.plant

/**
 * 设备列表后端返回来数据模型
 */
class DeviceListResultModel(
    val combinerList: Array<DeviceModel>?,//Combiner设备列表
    val datalogList: Array<DeviceModel>?,//采集器设备列表
    val pbdlist: Array<DeviceModel>?,//PBD设备列表
    val hpslist: Array<DeviceModel>?,//HPS设备列表
    val pcslist: Array<DeviceModel>?,//PCS设备列表
    val bmslist: Array<DeviceModel>?//BMS设备列表
)