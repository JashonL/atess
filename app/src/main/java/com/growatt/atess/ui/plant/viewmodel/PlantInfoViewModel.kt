package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.DeviceListResultModel
import com.growatt.atess.model.plant.DeviceModel
import com.growatt.atess.model.plant.PlantModel
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

    val getDeviceListLiveData = MutableLiveData<Pair<List<DeviceModel>, String?>>()

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
                        if (deviceList.size < 3 && !data?.bmslist.isNullOrEmpty()) {
                            deviceList.add(data?.bmslist!![0])
                        }
//                        if (deviceList.size < 3 && !data?.combinerList.isNullOrEmpty()) {
//                            deviceList.add(data?.combinerList!![0])
//                        }
                        if (deviceList.size < 3 && !data?.datalogList.isNullOrEmpty()) {
                            deviceList.add(data?.datalogList!![0])
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

}