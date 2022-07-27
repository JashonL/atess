package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import com.growatt.atess.model.plant.BmsModel
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult

class BmsViewModel : BaseDeviceInfoViewModel() {

    val getDeviceInfoLiveData = MutableLiveData<Pair<BmsModel?, String?>>()

    override fun getDeviceInfo(@DeviceType type: Int) {
        val params = hashMapOf<String, String>().apply {
            put("bmsSn", deviceSn ?: "")
        }
        apiService().postForm(ApiPath.Plant.GET_DEVICE_BMS_INFO, params, object :
            HttpCallback<HttpResult<BmsModel>>() {
            override fun success(result: HttpResult<BmsModel>) {
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