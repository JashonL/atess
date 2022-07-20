package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.DeviceListResultModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 添加采集器
 */
class DeviceListViewModel : BaseViewModel() {

    val getDeviceListLiveData = MutableLiveData<Pair<DeviceListResultModel?, String?>>()

    /**
     * 添加采集器
     */
    fun getDeviceList(plantId: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId)
            }
            apiService().postForm(ApiPath.Plant.GET_DEVICE_LIST, params, object :
                HttpCallback<HttpResult<DeviceListResultModel>>() {
                override fun success(result: HttpResult<DeviceListResultModel>) {
                    if (result.isBusinessSuccess()) {
                        getDeviceListLiveData.value = Pair(result.data, null)
                    } else {
                        getDeviceListLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getDeviceListLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }

}