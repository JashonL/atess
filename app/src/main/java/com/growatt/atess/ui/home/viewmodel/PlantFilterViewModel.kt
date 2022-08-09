package com.growatt.atess.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.growatt.atess.base.BaseViewModel

/**
 * 电站筛选
 */
class PlantFilterViewModel : BaseViewModel() {

    val getPlantFilterLiveData = MutableLiveData<Int>()

    /**
     * 设置排序类型
     */
    fun setFilterType(orderType: Int) {
        getPlantFilterLiveData.value = orderType
    }

}