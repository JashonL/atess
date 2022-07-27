package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.*
import com.growatt.atess.service.http.ApiPath
import com.growatt.atess.view.DateType
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.util.DateUtils
import kotlinx.coroutines.launch
import java.util.*

/**
 * 电站详情
 */
class PlantInfoViewModel : BaseViewModel() {

    var plantId: String? = null
    var plantModels: Array<PlantModel>? = null

    //图表数据请求参数
    var typeAndSn: Pair<Int, String>? = null
    var selectDate = Date()
    var dataType = "1" //dataType 1 —— 功率电量；2 —— soc; 3 —— 充放电
    var dateType = DateType.HOUR //dateType 1 —— 时；2 —— 日；3 —— 月；4 —— 年

    val getPlantInfoLiveData = MutableLiveData<Pair<PlantModel?, String?>>()

    val getPlantWeatherInfoLiveData = MutableLiveData<Pair<WeatherModel?, String?>>()

    val getDeviceListLiveData = MutableLiveData<Pair<List<DeviceModel>, String?>>()

    val getDeviceEnergyInfoLiveData = MutableLiveData<Pair<DeviceEnergyInfoModel?, String?>>()

    /**
     * 里面一层Pair，first是设备类型，second是设备序列号
     */
    val getPcsHpsSNLiveData = MutableLiveData<Pair<MutableList<Pair<Int, String>>?, String?>>()

    val getChartLiveData = MutableLiveData<Pair<ChartListDataModel?, String?>>()

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
    fun getEnergyInfo() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId ?: "")
                put("deviceSn", typeAndSn?.second ?: "")
                put("deviceType", if (typeAndSn?.first == DeviceType.HPS) "hps" else "pcs")
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

    /**
     * 获取HPS或PCS图表详情
     */
    fun getChartInfo() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId ?: "")
                put("deviceType", if (typeAndSn?.first == DeviceType.HPS) "hps" else "pcs")
                put("deviceSn", typeAndSn?.second ?: "")
                put("dataType", dataType)
                if (dataType == "2" || dataType == "3") {
                    //SOC与充放电类型
                    put("queryDate", DateUtils.yyyy_MM_dd_format(selectDate))
                    put("dateType", "1")
                } else {
                    //功率电量类型
                    if (dateType != "4") {
                        //年的情况就不需要传时间字段
                        put("queryDate", DateUtils.yyyy_MM_dd_format(selectDate))
                    }
                    put("dateType", dateType)
                }
            }
            apiService().postForm(ApiPath.Plant.GET_HPS_OR_PCS_CHART_INFO, params, object :
                HttpCallback<HttpResult<ChartListDataModel>>() {
                override fun success(result: HttpResult<ChartListDataModel>) {
                    if (result.isBusinessSuccess()) {
                        getChartLiveData.value = Pair(result.data, null)
                    } else {
                        getChartLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getChartLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }
}