package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 选择国家/地区
 */
class SelectAreaViewModel : BaseViewModel() {

    val areaListLiveData = MutableLiveData<Pair<Array<String>, String?>>()

    /**
     * 获取国家/地区列表
     */
    fun fetchAreaList() {
        viewModelScope.launch {
            apiService().httpGet(
                ApiPath.Mine.GET_COUNTRY_LIST,
                object : HttpCallback<HttpResult<Array<String>>>() {
                    override fun success(result: HttpResult<Array<String>>) {
                        if (result.isBusinessSuccess()) {
                            val countryList = result.data
                            if (countryList == null) {
                                areaListLiveData.value = Pair(emptyArray(), null)
                            } else {
                                areaListLiveData.value = Pair(countryList, null)
                            }
                        } else {
                            areaListLiveData.value = Pair(emptyArray(), result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        areaListLiveData.value = Pair(emptyArray(), error ?: "")
                    }
                })
        }
    }
}