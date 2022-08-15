package com.growatt.atess.ui.plant.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.AddPlantModel
import com.growatt.atess.model.plant.GeneralItemModel
import com.growatt.atess.model.plant.ProvinceModel
import com.growatt.atess.model.plant.TimeZone
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpErrorModel
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch
import java.io.File

/**
 * 添加电站
 */
class AddPlantViewModel : BaseViewModel() {

    var isEditMode: Boolean = false

    var addPlantModel = AddPlantModel()

    val timeZoneLiveData = MutableLiveData<Pair<TimeZone?, String?>>()

    val addPlantLiveData = MutableLiveData<Pair<String?, String?>>()

    val editPlantLiveData = MutableLiveData<String?>()

    val cityListLiveData = MutableLiveData<Pair<Array<ProvinceModel>?, String?>>()

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

                    override fun onFailure(errorModel: HttpErrorModel) {
                        timeZoneLiveData.value = Pair(null, errorModel.errorMsg ?: "")
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

                    override fun onFailure(errorModel: HttpErrorModel) {
                        currencyListLiveData.value = Pair(emptyArray(), errorModel.errorMsg ?: "")
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

                    override fun onFailure(errorModel: HttpErrorModel) {
                        cityListLiveData.value = Pair(emptyArray(), errorModel.errorMsg ?: "")
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
                if (TextUtils.isEmpty(addPlantModel.plantFileCompress)) null else File(addPlantModel.plantFileCompress!!),
                object : HttpCallback<HttpResult<AddPlantModel>>() {
                    override fun success(result: HttpResult<AddPlantModel>) {
                        if (result.isBusinessSuccess()) {
                            addPlantLiveData.value = Pair(result.data?.plantID, null)
                        } else {
                            addPlantLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        addPlantLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                    }

                })
        }
    }

    /**
     * 修改电站
     */
    fun editPlant() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("ID", addPlantModel.plantID!!)
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
                ApiPath.Plant.UPDATE_PLANT,
                params,
                if (TextUtils.isEmpty(addPlantModel.plantFileCompress)) null else File(addPlantModel.plantFileCompress!!),
                object : HttpCallback<HttpResult<AddPlantModel>>() {
                    override fun success(result: HttpResult<AddPlantModel>) {
                        if (result.isBusinessSuccess()) {
                            editPlantLiveData.value = null
                        } else {
                            editPlantLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        editPlantLiveData.value = errorModel.errorMsg ?: ""
                    }

                })
        }
    }
}