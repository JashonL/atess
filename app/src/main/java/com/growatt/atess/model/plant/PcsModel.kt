package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.model.plant.ui.IDeviceInfoHead
import com.growatt.lib.util.Util
import org.json.JSONObject

data class PcsModel(
    val deviceModel: String?,//设备型号
    val deviceSn: String?,//设备序列号
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

    companion object {
        /**
         * 创建图表类型(传给服务端的类型)
         *
         * 1 —— 光伏功率
         * 2 —— 电网功率
         * 3 —— 电池充电功率
         * 4 —— 电池放电功率
         */
        fun createChartType(): Array<ChartTypeModel> {
            return arrayOf(
                ChartTypeModel(
                    "1",
                    MainApplication.instance().getString(R.string.photovoltaic_power),
                    MainApplication.instance().getString(R.string.kw)
                ),
                ChartTypeModel(
                    "2",
                    MainApplication.instance().getString(R.string.grid_power),
                    MainApplication.instance().getString(R.string.kw)
                ),
                ChartTypeModel(
                    "3",
                    MainApplication.instance().getString(R.string.battery_charge_power),
                    MainApplication.instance().getString(R.string.kw)
                ),
                ChartTypeModel(
                    "4",
                    MainApplication.instance().getString(R.string.battery_discharge_power),
                    MainApplication.instance().getString(R.string.kw)
                )
            )
        }
    }

    override fun getIDeviceSn(): String {
        return MainApplication.instance().getString(R.string.sn_format, deviceSn)
    }

    override fun getIDeviceModel(): String {
        return MainApplication.instance().getString(R.string.model_format, deviceModel)
    }

    override fun getIDeviceParamsJsonStr(): String {
        val facText =
            "${Util.getDoubleText(vacFrequency)}${
                MainApplication.instance().getString(R.string.hz)
            }"
        val pacToBatteryText =
            "${Util.getDoubleText(pacToBattery)}${
                MainApplication.instance().getString(R.string.kw)
            }"
        val bApparentPowerText =
            "${Util.getDoubleText(bApparentPower)}${
                MainApplication.instance().getString(R.string.kva)
            }"
        val bActivePowerText =
            "${Util.getDoubleText(bActivePower)}${
                MainApplication.instance().getString(R.string.kw)
            }"
        val bReactivePowerText =
            "${Util.getDoubleText(bReactivePower)}${
                MainApplication.instance().getString(R.string.kvar)
            }"
        val loadPfText = Util.getDoubleText(pf)
        val envText = "${Util.getDoubleText(envTemp)}℃"
        val selfTimeText = "${selfTime}s"

        val json = JSONObject()
        json.put(MainApplication.instance().getString(R.string.output_frequency), facText)
        json.put(MainApplication.instance().getString(R.string.battery_power), pacToBatteryText)
        json.put(
            MainApplication.instance().getString(R.string.bypass_apparent_power),
            bApparentPowerText
        )
        json.put(
            MainApplication.instance().getString(R.string.bypass_active_power),
            bActivePowerText
        )
        json.put(
            MainApplication.instance().getString(R.string.bypass_reactive_power),
            bReactivePowerText
        )

        json.put(MainApplication.instance().getString(R.string.load_power_format), loadPfText)
        json.put(MainApplication.instance().getString(R.string.environment_temperature), envText)
        json.put(
            MainApplication.instance().getString(R.string.startup_self_test_time),
            selfTimeText
        )
        json.put(
            MainApplication.instance().getString(R.string.parallel_monitor_judgment_flag),
            typeFlag.toString()
        )
        return json.toString()
    }

    fun getTotalPowerText(): String {
        return Util.getDoubleText(ppv) + "W"
    }

}