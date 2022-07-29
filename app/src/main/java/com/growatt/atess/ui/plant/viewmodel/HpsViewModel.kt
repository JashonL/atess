package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.HpsModel
import com.growatt.atess.model.plant.HpsSystemOperationModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult

class HpsViewModel : BaseDeviceInfoViewModel() {

    val getDeviceInfoLiveData = MutableLiveData<Pair<HpsModel?, String?>>()

    val getSystemOperationInfoLiveData = MutableLiveData<Pair<HpsSystemOperationModel?, String?>>()

    override fun getDeviceInfo(@DeviceType type: Int) {
        val params = hashMapOf<String, String>().apply {
            put("hpsSn", deviceSn ?: "")
        }
        apiService().postForm(ApiPath.Plant.GET_DEVICE_HPS_INFO, params, object :
            HttpCallback<HttpResult<HpsModel>>() {
            override fun success(result: HttpResult<HpsModel>) {
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

    fun getHpsSystemOperationInfo(deviceSn: String?) {
        val params = hashMapOf<String, String>().apply {
            put("hpsSn", deviceSn ?: "")
        }
        apiService().postForm(ApiPath.Plant.GET_HPS_SYSTEM_OPERATION, params, object :
            HttpCallback<HttpResult<HpsSystemOperationModel>>() {
            override fun success(result: HttpResult<HpsSystemOperationModel>) {
                if (result.isBusinessSuccess()) {
                    getSystemOperationInfoLiveData.value = Pair(result.data, null)
                } else {
                    getSystemOperationInfoLiveData.value = Pair(null, result.msg ?: "")
                }
            }

            override fun onFailure(error: String?) {
                super.onFailure(error)
                getSystemOperationInfoLiveData.value = Pair(null, error ?: "")
            }
        })
    }


}