package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.AddPlantModel
import com.growatt.atess.model.plant.GeneralItemModel
import com.growatt.atess.model.plant.TimeZone
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

    val timeZoneLiveData = MutableLiveData<Pair<TimeZone?, String?>>()

    val currencyListLiveData = MutableLiveData<Pair<Array<GeneralItemModel>, String?>>()

    /**
     * 获取时区列表
     */
    fun fetchTimeZoneList(country: String) {
        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Plant.GET_TIMEZONE_BY_COUNTRY,
                hashMapOf(Pair("country", country)),
                object : HttpCallback<HttpResult<TimeZone>>() {
                    override fun success(result: HttpResult<TimeZone>) {
                        if (result.isBusinessSuccess()) {
                            timeZoneLiveData.value = Pair(result.data, null)
                        } else {
                            timeZoneLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        timeZoneLiveData.value = Pair(null, error ?: "")
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