package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.model.plant.ui.IDeviceInfoHead
import org.json.JSONObject

data class PcsModel(
    val deviceModel: String?,//设备型号
    val pcsid: String?,//设备序列号
    val realType: Int = DeviceType.PCS,
    val eToday: Double?,//今日发电量
    val eTotal: Double?,//累计发电量
    val lost: Boolean?,//是否离线
    val ppv: Double?,//发电功率
    val vacFrequency: Double?,//输出频率
    val pacToBattery: Double?,//电池功率
    val bApparentPower: Double?,//旁路视在功率
    val bActivePower: Double?,//旁路有功功率
    val bReactivePower: Double?,//旁路无功功率
    val pf: Double?,//功率因数
    val envTemp: Double?,//环境温度
    val selfTime: Int?,//开机自检时间
    val typeFlag: Int?,//监控并机判断标志
) : IDeviceInfoHead {
    override fun getIDeviceSn(): String {
        return MainApplication.instance().getString(R.string.sn_format, pcsid)
    }

    override fun getIDeviceModel(): String {
        return MainApplication.instance().getString(R.string.model_format, deviceModel)
    }

    override fun getIDeviceParamsJsonStr(): String {
//        val vpvText = "${Util.getDoubleText(vpv)}V"
//        val facText =
//            "${Util.getDoubleText(fac)}${MainApplication.instance().getString(R.string.kwh)}"
//        val envText = "${Util.getDoubleText(envTemp)}℃"
//        val pac2Text =
//            "${Util.getDoubleText(pac2)}${MainApplication.instance().getString(R.string.kwh)}"
//        val selfTimeText = "${selfTime}s"
//        val loadActivePowerText = "${Util.getDoubleText(loadActivePower)}${
//            MainApplication.instance().getString(R.string.kwh)
//        }"
//        val loadReactivePowerText = "${Util.getDoubleText(loadReactivePower)}${
//            MainApplication.instance().getString(R.string.kwh)
//        }"
//        val loadPfText = Util.getDoubleText(loadPf)
//
        val json = JSONObject()
//        json.put(MainApplication.instance().getString(R.string.output_voltage), vpvText)
//        json.put(MainApplication.instance().getString(R.string.output_frequency), facText)
//        json.put(MainApplication.instance().getString(R.string.environment_temperature), envText)
//        json.put(
//            MainApplication.instance().getString(R.string.startup_self_test_time),
//            selfTimeText
//        )
//        json.put(MainApplication.instance().getString(R.string.load_time_power), pac2Text)
//        json.put(
//            MainApplication.instance().getString(R.string.load_active_power),
//            loadActivePowerText
//        )
//        json.put(
//            MainApplication.instance().getString(R.string.load_reactive_power),
//            loadReactivePowerText
//        )
//        json.put(MainApplication.instance().getString(R.string.load_power_factor), loadPfText)

        return json.toString()
    }

}