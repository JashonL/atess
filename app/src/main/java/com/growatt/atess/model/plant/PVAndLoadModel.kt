package com.growatt.atess.model.plant

import com.growatt.atess.util.ValueUtil

/**
 * 总览-光伏产出和负载用电
 */
data class PVAndLoadModel(
    val pvout: Double,//光伏产出
    val pvSelfUse: Double,//光伏产出 - 自发自用
    val toGrid: Double,//光伏产出 - 馈入电网
    val load: Double,//负载用电
    val loadSelfUse: Double,//负载用电-自发自用
    val fromDg: Double,//负载用电-油机产出
    val fromGrid: Double//负载用电-电网取电
) {
    fun getPVOutWithUnit(): Pair<String, String> {
        return ValueUtil.valueFromKWh(pvout)
    }

    fun getPVSelfWithUnit(): Pair<String, String> {
        return ValueUtil.valueFromKWh(pvSelfUse)
    }

    fun getPVSelfRatio(): Int {
        return (pvSelfUse / pvout * 100).toInt()
    }

    fun getPVSelfPercentText(): String {
        return getPVSelfRatio().toString() + "%"
    }

    fun getPVToGridWithUnit(): Pair<String, String> {
        return ValueUtil.valueFromKWh(toGrid)
    }

    fun getPVToGridPercentText(): String {
        return getPVToGridRatio().toString() + "%"
    }

    fun getPVToGridRatio(): Int {
        return if (toGrid == 0.0) 0 else 100 - getPVSelfRatio()
    }

    fun getLoadWithUnit(): Pair<String, String> {
        return ValueUtil.valueFromKWh(load)
    }

    fun getLoadSelfRatio(): Int {
        return (loadSelfUse / load * 100).toInt()
    }

    fun getLoadSelfUseWithUnit(): Pair<String, String> {
        return ValueUtil.valueFromKWh(loadSelfUse)
    }

    fun getLoadSelfUsePercentText(): String {
        return getLoadSelfRatio().toString() + "%"
    }

    fun getLoadFromOilEngineRatio(): Int {
        return (fromDg / load * 100).toInt()
    }

    fun getLoadFromOilEngineWithUnit(): Pair<String, String> {
        return ValueUtil.valueFromKWh(fromDg)
    }

    fun getLoadFromOilEnginePercentText(): String {
        return getLoadFromOilEngineRatio().toString() + "%"
    }

    fun getLoadFromGridWithUnit(): Pair<String, String> {
        return ValueUtil.valueFromKWh(fromGrid)
    }

    fun getLoadFromGridPercentText(): String {
        return getLoadFromGridPercentRadio().toString() + "%"
    }

    fun getLoadFromGridPercentRadio(): Int {
        return if (fromGrid == 0.0) 0 else 100 - getLoadSelfRatio() - getLoadFromOilEngineRatio()
    }
}