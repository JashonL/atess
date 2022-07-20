package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

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
) {
    fun getTabsText(): MutableList<String> {
        val tabsText = mutableListOf<String>()
        tabsText.add(MainApplication.instance().getString(R.string.hps))
        tabsText.add(MainApplication.instance().getString(R.string.pcs))
        tabsText.add(MainApplication.instance().getString(R.string.pbd))
        tabsText.add(MainApplication.instance().getString(R.string.bms))
        tabsText.add(MainApplication.instance().getString(R.string.collector))
        return tabsText
    }
}