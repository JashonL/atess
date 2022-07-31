package com.growatt.atess.ui.plant.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.DeviceListResultModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 我的设备列表
 */
class DeviceListViewModel : BaseViewModel() {


    val getDeviceListLiveData = MutableLiveData<Pair<DeviceListResultModel?, String?>>()

    /**
     * 获取我的设备列表
     */
    fun getDeviceList(plantId: String?, searchWord: String = "") {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId ?: "")
                if (!TextUtils.isEmpty(searchWord)) {
                    put("searchWord", searchWord)
                }
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