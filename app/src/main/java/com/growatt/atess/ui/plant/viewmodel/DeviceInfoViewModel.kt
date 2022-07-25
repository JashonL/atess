package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.model.plant.ChartTypeModel
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.util.DateUtils
import kotlinx.coroutines.launch
import java.util.*

/**
 * 设备详情ViewModel
 * 泛型T是对应设备详情Model
 */
class DeviceInfoViewModel<T> : BaseViewModel() {

    var deviceSn: String? = null
    var chartType: ChartTypeModel? = null
    var selectDate: Long = Date().time

    val getDeviceInfoLiveData = MutableLiveData<Pair<T?, String?>>()

    val getDeviceChartLiveData = MutableLiveData<Pair<ChartListDataModel?, String?>>()

    /**
     * 获取设备详情
     */
    fun getDeviceInfo(@DeviceType type: Int) {
        var requestUrl = ""
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                when (type) {
                    DeviceType.HPS -> {
                        put("hpsSn", deviceSn ?: "")
                        requestUrl = ApiPath.Plant.GET_DEVICE_HPS_INFO
                    }
                    DeviceType.PCS -> {
                        put("pcsSn", deviceSn ?: "")
                        requestUrl = ApiPath.Plant.GET_DEVICE_PCS_INFO
                    }
                    DeviceType.PBD -> {
                        put("pbdSn", deviceSn ?: "")
                        requestUrl = ApiPath.Plant.GET_DEVICE_PBD_INFO
                    }
                }
            }
            apiService().postForm(requestUrl, params, object :
                HttpCallback<HttpResult<T>>() {
                override fun success(result: HttpResult<T>) {
                    if (result.isBusinessSuccess()) {
                        getDeviceInfoLiveData.value = Pair(result.data, null)
                    } else {
                        getDeviceInfoLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getDeviceInfoLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }

    /**
     * 获取图表详情
     * @param queryDate 2022-07-06
     * @param dataType 图表类型
     */
    fun getDeviceChartInfo(@DeviceType deviceType: Int) {
        var requestUrl = ""
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                when (deviceType) {
                    DeviceType.HPS -> {
                        put("hpsSn", deviceSn ?: "")
                        requestUrl = ApiPath.Plant.GET_DEVICE_HPS_CHART_INFO
                    }
                    DeviceType.PCS -> {
                        put("pcsSn", deviceSn ?: "")
                        requestUrl = ApiPath.Plant.GET_DEVICE_PCS_CHART_INFO
                    }
                    DeviceType.PBD -> {
                        put("pbdSn", deviceSn ?: "")
                        requestUrl = ApiPath.Plant.GET_DEVICE_PBD_CHART_INFO
                    }
                }
                put("queryDate", DateUtils.yyyy_MM_dd_format(selectDate))
                put("dataType", chartType?.type ?: "")
            }
            apiService().postForm(requestUrl, params, object :
                HttpCallback<HttpResult<ChartListDataModel>>() {
                override fun success(result: HttpResult<ChartListDataModel>) {
                    if (result.isBusinessSuccess()) {
                        getDeviceChartLiveData.value = Pair(result.data, null)
                    } else {
                        getDeviceChartLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getDeviceChartLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }

}