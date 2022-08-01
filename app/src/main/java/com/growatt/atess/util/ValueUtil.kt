package com.growatt.atess.util

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.lib.util.Util


object ValueUtil {
    /**
     * 数值转换，基础单位是kWh
     */
    fun valueFromKWh(kwhValue: Double?): Pair<String, String> {
        return when {
            kwhValue == null -> {
                Pair("0", MainApplication.instance().getString(R.string.kwh))
            }
            kwhValue < 100000 -> {
                Pair(
                    Util.getDoubleText(kwhValue),
                    MainApplication.instance().getString(R.string.kwh)
                )
            }
            else -> {
                Pair(
                    Util.getDoubleText(kwhValue / 1000),
                    MainApplication.instance().getString(R.string.mwh)
                )
            }
        }
    }

    /**
     * 数值转换，基础单位是w
     */
    fun valueFromW(wValue: Double?): Pair<String, String> {
        return when {
            wValue == null -> {
                Pair("0", MainApplication.instance().getString(R.string.kw))
            }
            wValue < 1000000000 -> {
                Pair(
                    Util.getDoubleText(wValue / 1000),
                    MainApplication.instance().getString(R.string.kw)
                )
            }
            else -> {
                Pair(
                    Util.getDoubleText(wValue / 1000000),
                    MainApplication.instance().getString(R.string.mw)
                )
            }
        }
    }

    /**
     * 数值转换，基础单位是w
     */
    fun valueFromWp(wValue: Double?): Pair<String, String> {
        return when {
            wValue == null -> {
                Pair("0", MainApplication.instance().getString(R.string.kwp))
            }
            wValue < 100000000 -> {
                Pair(
                    Util.getDoubleText(wValue / 1000),
                    MainApplication.instance().getString(R.string.kwp)
                )
            }
            else -> {
                Pair(
                    Util.getDoubleText(wValue / 1000000),
                    MainApplication.instance().getString(R.string.mwp)
                )
            }
        }
    }

    /**
     * 数值转换，基础单位是kg
     */
    fun valueFromKG(kgValue: Double?): Pair<String, String> {
        return when {
            kgValue == null -> {
                Pair("0", MainApplication.instance().getString(R.string.kg))
            }
            kgValue < 100000 -> {
                Pair(
                    Util.getDoubleText(kgValue),
                    MainApplication.instance().getString(R.string.kg)
                )
            }
            else -> {
                Pair(
                    Util.getDoubleText(kgValue / 1000),
                    MainApplication.instance().getString(R.string.t)
                )
            }
        }
    }
}