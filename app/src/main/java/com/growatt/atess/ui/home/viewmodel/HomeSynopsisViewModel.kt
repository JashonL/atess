package com.growatt.atess.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.SynopsisTotalModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 首页-总览
 */
class HomeSynopsisViewModel : BaseViewModel() {

    val getSynopsisTotalLiveData = MutableLiveData<Pair<SynopsisTotalModel?, String?>>()

    /**
     * 获取总览信息
     */
    fun getSynopsisTotal() {
        viewModelScope.launch {
            apiService().post(
                ApiPath.Plant.GET_SYNOPSIS_TOTAL,
                object : HttpCallback<HttpResult<SynopsisTotalModel>>() {
                    override fun success(result: HttpResult<SynopsisTotalModel>) {
                        if (result.isBusinessSuccess()) {
                            getSynopsisTotalLiveData.value = Pair(result.data, null)
                        } else {
                            getSynopsisTotalLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        getSynopsisTotalLiveData.value = Pair(null, error ?: "")
                    }
                })
        }
    }
}