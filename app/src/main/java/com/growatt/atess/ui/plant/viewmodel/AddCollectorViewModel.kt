package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 添加采集器
 */
class AddCollectorViewModel : BaseViewModel() {

    var plantId: String? = null

    val addCollectorLiveData = MutableLiveData<String?>()

    /**
     * 添加采集器
     */
    fun addCollector(collectorSN: String, checkCode: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("deviceSN", collectorSN)
                put("validCode", checkCode)
                put("plantID", plantId ?: "")
            }
            apiService().postForm(ApiPath.Plant.ADD_COLLECTOR, params, object :
                HttpCallback<HttpResult<String>>() {
                override fun success(result: HttpResult<String>) {
                    if (result.isBusinessSuccess()) {
                        addCollectorLiveData.value = null
                    } else {
                        addCollectorLiveData.value = result.msg ?: ""
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    addCollectorLiveData.value = error ?: ""
                }
            })
        }
    }


}