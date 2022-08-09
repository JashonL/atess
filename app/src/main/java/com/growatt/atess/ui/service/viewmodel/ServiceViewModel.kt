package com.growatt.atess.ui.service.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.ServiceModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpErrorModel
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 添加采集器
 */
class ServiceViewModel : BaseViewModel() {

    val getServiceManualLiveData = MutableLiveData<Pair<Array<ServiceModel>?, String?>>()

    val getInstallVideoLiveData = MutableLiveData<Pair<Array<ServiceModel>?, String?>>()

    /**
     * 获取使用手册列表
     */
    fun getServiceManual() {
        viewModelScope.launch {
            apiService().post(ApiPath.Service.GET_SERVICE_MANUAL, object :
                HttpCallback<HttpResult<Array<ServiceModel>>>() {
                override fun success(result: HttpResult<Array<ServiceModel>>) {
                    if (result.isBusinessSuccess()) {
                        getServiceManualLiveData.value = Pair(result.data, null)
                    } else {
                        getServiceManualLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(errorModel: HttpErrorModel) {
                    getServiceManualLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                }

            })
        }
    }

    /**
     * 获取安装列表
     */
    fun getInstallVideo() {
        viewModelScope.launch {
            apiService().post(ApiPath.Service.GET_INSTALL_VIDEO, object :
                HttpCallback<HttpResult<Array<ServiceModel>>>() {
                override fun success(result: HttpResult<Array<ServiceModel>>) {
                    if (result.isBusinessSuccess()) {
                        getInstallVideoLiveData.value = Pair(result.data, null)
                    } else {
                        getInstallVideoLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(errorModel: HttpErrorModel) {
                    getInstallVideoLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                }

            })
        }
    }


}