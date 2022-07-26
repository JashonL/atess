package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.*
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 电站详情
 */
class PlantInfoViewModel : BaseViewModel() {

    var plantId: String? = null
    var plantModels: Array<PlantModel>? = null

    val getPlantInfoLiveData = MutableLiveData<Pair<PlantModel?, String?>>()

    val getPlantWeatherInfoLiveData = MutableLiveData<Pair<WeatherModel?, String?>>()

    val getDeviceListLiveData = MutableLiveData<Pair<List<DeviceModel>, String?>>()

    val getDeviceEnergyInfoLiveData = MutableLiveData<Pair<DeviceEnergyInfoModel?, String?>>()

    /**
     * 里面一层Pair，first是设备类型，second是设备序列号
     */
    val getPcsHpsSNLiveData = MutableLiveData<Pair<MutableList<Pair<Int, String>>?, String?>>()

    /**
     * 获取电站详情
     */
    fun getPlantInfo() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId ?: "")
            }
            apiService().postForm(ApiPath.Plant.GET_PLANT_INFO, params, object :
                HttpCallback<HttpResult<PlantInfoResultModel>>() {
                override fun success(result: HttpResult<PlantInfoResultModel>) {
                    if (result.isBusinessSuccess()) {
                        getPlantInfoLiveData.value = Pair(result.data?.plantBean, null)
                        getPlantWeatherInfoLiveData.value = Pair(result.data?.weatherMap, null)
                    } else {
                        getPlantInfoLiveData.value = Pair(null, result.msg ?: "")
                        getPlantWeatherInfoLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getPlantInfoLiveData.value = Pair(null, error ?: "")
                    getPlantWeatherInfoLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }

    /**
     * 获取设备列表，每种类型的设备取第一个，最多3个
     */
    fun getDeviceList() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId ?: "")
            }
            apiService().postForm(ApiPath.Plant.GET_DEVICE_LIST, params, object :
                HttpCallback<HttpResult<DeviceListResultModel>>() {
                override fun success(result: HttpResult<DeviceListResultModel>) {
                    if (result.isBusinessSuccess()) {
                        val deviceList = mutableListOf<DeviceModel>()
                        val data = result.data
                        if (deviceList.size < 3 && !data?.hpslist.isNullOrEmpty()) {
                            deviceList.add(data?.hpslist!![0])
                        }
                        if (deviceList.size < 3 && !data?.pcslist.isNullOrEmpty()) {
                            deviceList.add(data?.pcslist!![0])
                        }
                        if (deviceList.size < 3 && !data?.pbdlist.isNullOrEmpty()) {
                            deviceList.add(data?.pbdlist!![0])
                        }
                        getDeviceListLiveData.value = Pair(deviceList, null)
                    } else {
                        getDeviceListLiveData.value = Pair(emptyList(), result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getDeviceListLiveData.value = Pair(emptyList(), error ?: "")
                }
            })
        }
    }

    /**
     * 获取PCS和HPS设备序列号
     */
    fun getPcsHpsSN() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId ?: "")
            }
            apiService().postForm(ApiPath.Plant.GET_PCS_AND_HPS_LIST, params, object :
                HttpCallback<HttpResult<PcsHpsSNModel>>() {
                override fun success(result: HttpResult<PcsHpsSNModel>) {
                    if (result.isBusinessSuccess()) {
                        val snList = mutableListOf<Pair<Int, String>>()
                        val hpsList = result.data?.hps
                        if (!hpsList.isNullOrEmpty()) {
                            for (hpsSN in hpsList) {
                                snList.add(
                                    Pair(
                                        DeviceType.HPS,
                                        hpsSN
                                    )
                                )
                            }
                        }
                        val pcsList = result.data?.pcs
                        if (!pcsList.isNullOrEmpty()) {
                            for (pcsSN in pcsList) {
                                snList.add(
                                    Pair(
                                        DeviceType.PCS,
                                        pcsSN
                                    )
                                )
                            }
                        }
                        getPcsHpsSNLiveData.value = Pair(snList, null)
                    } else {
                        getPcsHpsSNLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getPcsHpsSNLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }

    /**
     * 获取HPS或PCS能源概况
     */
    fun getEnergyInfo(deviceType: Int, deviceSn: String?) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId ?: "")
                put("deviceSn", deviceSn ?: "")
                put("deviceType", if (deviceType == DeviceType.HPS) "hps" else "pcs")
            }
            apiService().postForm(ApiPath.Plant.GET_ENERGY_INFO, params, object :
                HttpCallback<HttpResult<DeviceEnergyInfoModel>>() {
                override fun success(result: HttpResult<DeviceEnergyInfoModel>) {
                    if (result.isBusinessSuccess()) {
                        getDeviceEnergyInfoLiveData.value = Pair(result.data, null)
                    } else {
                        getDeviceEnergyInfoLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getDeviceEnergyInfoLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }
}