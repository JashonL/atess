package com.growatt.atess.model.plant.ui

/**
 * HPS、PCS、PBD设备头部数据接口
 */
interface IDeviceInfoHead {
    /**
     * 设备序列号
     */
    fun getIDeviceSn(): String

    /**
     * 设备型号
     */
    fun getIDeviceModel(): String

    /**
     * 全部参数String字符串
     */
    fun getIDeviceParamsJsonStr(): String
}