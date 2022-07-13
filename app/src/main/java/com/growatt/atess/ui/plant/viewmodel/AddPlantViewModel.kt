package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.AddPlantModel
import com.growatt.atess.model.plant.GeneralItemModel
import com.growatt.atess.model.plant.ProvinceModel
import com.growatt.atess.model.plant.TimeZone
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch
import java.io.File

/**
 * 添加电站
 */
class AddPlantViewModel : BaseViewModel() {

    var isEditMode: Boolean = false

    val addPlantModel = AddPlantModel()

    val timeZoneLiveData = MutableLiveData<Pair<TimeZone?, String?>>()

    val addPlantLiveData = MutableLiveData<Pair<String?, String?>>()

    val cityListLiveData = MutableLiveData<Pair<Array<ProvinceModel>, String?>>()

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

    /**
     * 获取城市列表
     */
    fun fetchCityList(country: String) {
        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Plant.GET_CITY_LIST,
                hashMapOf(Pair("country", country)),
                object : HttpCallback<HttpResult<Array<ProvinceModel>>>() {
                    override fun success(result: HttpResult<Array<ProvinceModel>>) {
                        if (result.isBusinessSuccess()) {
                            val provinceList = result.data
                            if (provinceList == null) {
                                cityListLiveData.value = Pair(emptyArray(), null)
                            } else {
                                cityListLiveData.value = Pair(provinceList, null)
                            }
                        } else {
                            cityListLiveData.value = Pair(emptyArray(), result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        cityListLiveData.value = Pair(emptyArray(), error ?: "")
                    }
                })
        }
    }

    /**
     * 新增电站
     */
    fun addPlant() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantName", addPlantModel.plantName!!)
                put("installDate", addPlantModel.getDateString())
                put("country", addPlantModel.country!!)
                put("city", addPlantModel.city!!)
                addPlantModel.plantAddress?.also {
                    put("plantAddress", it)
                }
                addPlantModel.plant_lat?.also {
                    put("plant_lat", it.toString())
                }
                addPlantModel.plant_lng?.also {
                    put("plant_lng", it.toString())
                }
                put("plantTimeZone", addPlantModel.plantTimeZone ?: "")
                put("nominalPower", addPlantModel.totalPower!!)
                put("formulaMoney", addPlantModel.formulaMoney ?: "0")
                put("formulaMoneyUnitId", addPlantModel.formulaMoneyUnitId ?: "")
            }

            apiService().postFile(
                ApiPath.Plant.ADD_PLANT,
                params,
                File(addPlantModel.plantFileCompress!!),
                object : HttpCallback<HttpResult<AddPlantModel>>() {
                    override fun success(result: HttpResult<AddPlantModel>) {
                        if (result.isBusinessSuccess()) {
                            addPlantLiveData.value = Pair(result.data?.plantID, null)
                        } else {
                            addPlantLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        addPlantLiveData.value = Pair(null, error ?: "")
                    }
                })
        }
    }
}