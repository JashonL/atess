package com.growatt.atess.ui.plant.viewmodel

import com.growatt.atess.base.BaseViewModel
import com.growatt.lib.util.DateUtils
import java.util.*

/**
 * 添加电站
 */
class AddPlantViewModel : BaseViewModel() {

    var isEditMode: Boolean = false

    var date: Date? = null

    var plantName = ""

    fun getDateString(): String {
        if (date == null) {
            date = Date()
        }
        return DateUtils.yyyy_MM_dd_format(date!!)
    }

}