package com.growatt.atess.model.plant

import com.growatt.lib.util.Util

data class DeviceEnergyInfoModel(
    val pvout: Array<Double>?,//光伏产出
    val oilout: Array<Double>?,//柴油机电机产出
    val batCharge: Array<Double>?,//电池充电
    val batDisCharge: Array<Double>?,//电池放电
    val load: Array<Double>?,//负载消耗
    val toGrid: Array<Double>?,// 馈入电网
    val fromGrid: Array<Double>?,// 电网取电
    val inverterOut: Array<Double>? // 并网逆变器产出
) {

    fun getFormatValues(values: Array<Double>?): Pair<String, String> {
        if (values == null) {
            return Pair("0", "0")
        }
        return Pair(Util.getDoubleText(values[0]), Util.getDoubleText(values[1]))
    }

    /**
     * 是否有光伏产出
     */
    fun hasPhotovoltaicOut(): Boolean {
        return pvout != null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceEnergyInfoModel

        if (!pvout.contentEquals(other.pvout)) return false
        if (!oilout.contentEquals(other.oilout)) return false
        if (!batCharge.contentEquals(other.batCharge)) return false
        if (!batDisCharge.contentEquals(other.batDisCharge)) return false
        if (!load.contentEquals(other.load)) return false
        if (!toGrid.contentEquals(other.toGrid)) return false
        if (!fromGrid.contentEquals(other.fromGrid)) return false
        if (!inverterOut.contentEquals(other.inverterOut)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pvout.contentHashCode()
        result = 31 * result + oilout.contentHashCode()
        result = 31 * result + batCharge.contentHashCode()
        result = 31 * result + batDisCharge.contentHashCode()
        result = 31 * result + load.contentHashCode()
        result = 31 * result + toGrid.contentHashCode()
        result = 31 * result + fromGrid.contentHashCode()
        result = 31 * result + inverterOut.contentHashCode()
        return result
    }
}
