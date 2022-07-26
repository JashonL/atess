package com.growatt.atess.ui.plant.activity

import android.content.Context
import com.growatt.atess.model.plant.DeviceType

/**
 * 设备详情基类
 */
interface IBaseDeviceActivity {

    companion object {
        /**
         * 跳转到设备详情页面
         */
        fun jumpToDeviceInfoPage(
            context: Context?,
            @DeviceType deviceType: Int,
            deviceSN: String?
        ) {
            when (deviceType) {
                DeviceType.HPS -> HpsInfoActivity.start(context, deviceSN)
                DeviceType.PCS -> PcsInfoActivity.start(context, deviceSN)
                DeviceType.PBD -> PbdInfoActivity.start(context, deviceSN)
                DeviceType.BMS -> BmsInfoActivity.start(context, deviceSN)
                DeviceType.MBMS -> MBmsInfoActivity.start(context, deviceSN)
                DeviceType.BCU_BMS -> BcuBmsInfoActivity.start(context, deviceSN)
            }
        }
    }

    fun getDeviceType(): Int
}