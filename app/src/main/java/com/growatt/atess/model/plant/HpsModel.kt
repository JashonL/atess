package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.model.plant.ui.IDeviceInfoHead
import com.growatt.lib.util.Util
import org.json.JSONObject

data class HpsModel(
    val deviceModel: String?,//设备型号
    val hpsid: String?,//设备序列号
    val realType: Int = DeviceType.HPS,
    val vpv: Double?,//输出电压
    val fac: Double?,//输出频率
    val envTemp: Double?,//环境温度
    val selfTime: Int?,//开机自检时间
    val pac2: Double?,//负载视在功率
    val loadActivePower: Double?,//负载有功功率
    val loadReactivePower: Double?,//负载无功功率
    val loadPf: Double?,//负载功率因数
) : IDeviceInfoHead {

    companion object {
        /**
         * 创建图表类型(传给服务端的类型)
         *
         * 1 —— pv功率
         * 2 —— 电网功率
         * 3 —— 电池充电功率
         * 4 —— 电池放电功率
         * 5 —— 负载功率
         * 6 —— pv电压
         * 7 —— 电池充电电压
         * 8 —— 电池放电电压
         * 9 —— pv电流
         * 10 —— 电池充电电流
         * 11 —— 电池放电电流
         * pair.first-类型，pair.second-描述
         */
        fun createChartType(): Array<Pair<String, String>> {
            return arrayOf(
                Pair("1", MainApplication.instance().getString(R.string.pv_power)),
                Pair("2", MainApplication.instance().getString(R.string.grid_power)),
                Pair("3", MainApplication.instance().getString(R.string.battery_charge_power)),
                Pair("4", MainApplication.instance().getString(R.string.battery_discharge_power)),
                Pair("5", MainApplication.instance().getString(R.string.load_power)),
                Pair("6", MainApplication.instance().getString(R.string.pv_voltage)),
                Pair("7", MainApplication.instance().getString(R.string.battery_charge_voltage)),
                Pair("8", MainApplication.instance().getString(R.string.battery_discharge_voltage)),
                Pair("9", MainApplication.instance().getString(R.string.pv_electricity)),
                Pair(
                    "10",
                    MainApplication.instance().getString(R.string.battery_charge_electricity)
                ),
                Pair(
                    "11",
                    MainApplication.instance().getString(R.string.battery_discharge_electricity)
                )
            )
        }
    }

    override fun getIDeviceSn(): String {
        return MainApplication.instance().getString(R.string.sn_format, hpsid)
    }

    override fun getIDeviceModel(): String {
        return MainApplication.instance().getString(R.string.model_format, deviceModel)
    }

    override fun getIDeviceParamsJsonStr(): String {
        val vpvText = "${Util.getDoubleText(vpv)}V"
        val facText =
            "${Util.getDoubleText(fac)}${MainApplication.instance().getString(R.string.kwh)}"
        val envText = "${Util.getDoubleText(envTemp)}℃"
        val pac2Text =
            "${Util.getDoubleText(pac2)}${MainApplication.instance().getString(R.string.kwh)}"
        val selfTimeText = "${selfTime}s"
        val loadActivePowerText = "${Util.getDoubleText(loadActivePower)}${
            MainApplication.instance().getString(R.string.kwh)
        }"
        val loadReactivePowerText = "${Util.getDoubleText(loadReactivePower)}${
            MainApplication.instance().getString(R.string.kwh)
        }"
        val loadPfText = Util.getDoubleText(loadPf)

        val json = JSONObject()
        json.put(MainApplication.instance().getString(R.string.output_voltage), vpvText)
        json.put(MainApplication.instance().getString(R.string.output_frequency), facText)
        json.put(MainApplication.instance().getString(R.string.environment_temperature), envText)
        json.put(
            MainApplication.instance().getString(R.string.startup_self_test_time),
            selfTimeText
        )
        json.put(MainApplication.instance().getString(R.string.load_time_power), pac2Text)
        json.put(
            MainApplication.instance().getString(R.string.load_active_power),
            loadActivePowerText
        )
        json.put(
            MainApplication.instance().getString(R.string.load_reactive_power),
            loadReactivePowerText
        )
        json.put(MainApplication.instance().getString(R.string.load_power_factor), loadPfText)

        return json.toString()
    }

}