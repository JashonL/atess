package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.AddPlantModel
import com.growatt.atess.model.plant.GeneralItemModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 添加电站
 */
class AddPlantViewModel : BaseViewModel() {

    var isEditMode: Boolean = false

    val addPlantModel = AddPlantModel()

    val timeZoneListLiveData = MutableLiveData<Pair<Array<GeneralItemModel>, String?>>()

    val currencyListLiveData = MutableLiveData<Pair<Array<GeneralItemModel>, String?>>()

    /**
     * 获取时区列表
     */
    fun fetchTimeZoneList() {
        viewModelScope.launch {
            apiService().httpGet(
                ApiPath.Plant.GET_TIME_ZONE_LIST,
                object : HttpCallback<HttpResult<Array<String>>>() {
                    override fun success(result: HttpResult<Array<String>>) {
                        if (result.isBusinessSuccess()) {
                            val timeZoneList = result.data
                            if (timeZoneList == null) {
                                timeZoneListLiveData.value = Pair(emptyArray(), null)
                            } else {
                                timeZoneListLiveData.value =
                                    Pair(GeneralItemModel.convert(timeZoneList), null)
                            }
                        } else {
                            timeZoneListLiveData.value = Pair(emptyArray(), result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        timeZoneListLiveData.value = Pair(emptyArray(), error ?: "")
                    }
                })
        }
    }

    /**
     * 获取货币列表
     */
    fun fetchCurrencyList() {
        viewModelScope.launch {
            apiService().httpGet(
                ApiPath.Plant.CURRENCY_LIST,
                object : HttpCallback<HttpResult<Array<String>>>() {
                    override fun success(result: HttpResult<Array<String>>) {
                        if (result.isBusinessSuccess()) {
                            val currencyList = result.data
                            if (currencyList == null) {
                                currencyListLiveData.value = Pair(emptyArray(), null)
                            } else {
                                currencyListLiveData.value =
                                    Pair(GeneralItemModel.convert(currencyList), null)
                            }
                        } else {
                            currencyListLiveData.value = Pair(emptyArray(), result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        currencyListLiveData.value = Pair(emptyArray(), error ?: "")
                    }
                })
        }
    }

}