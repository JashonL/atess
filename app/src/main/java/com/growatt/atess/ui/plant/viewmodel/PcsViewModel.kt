package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.PcsModel
import com.growatt.atess.model.plant.ui.PcsSystemOperationModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult

class PcsViewModel : BaseDeviceInfoViewModel() {

    val getDeviceInfoLiveData = MutableLiveData<Pair<PcsModel?, String?>>()

    val getSystemOperationInfoLiveData = MutableLiveData<Pair<PcsSystemOperationModel?, String?>>()

    override fun getDeviceInfo(@DeviceType type: Int) {
        val params = hashMapOf<String, String>().apply {
            put("pcsSn", deviceSn ?: "")
        }
        apiService().postForm(ApiPath.Plant.GET_DEVICE_PCS_INFO, params, object :
            HttpCallback<HttpResult<PcsModel>>() {
            override fun success(result: HttpResult<PcsModel>) {
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

    fun getPcsSystemOperationInfo(deviceSn: String?) {
        val params = hashMapOf<String, String>().apply {
            put("pcsSn", deviceSn ?: "")
        }
        apiService().postForm(ApiPath.Plant.GET_PCS_SYSTEM_OPERATION, params, object :
            HttpCallback<HttpResult<PcsSystemOperationModel>>() {
            override fun success(result: HttpResult<PcsSystemOperationModel>) {
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