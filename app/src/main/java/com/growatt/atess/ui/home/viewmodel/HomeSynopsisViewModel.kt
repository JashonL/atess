package com.growatt.atess.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.model.plant.PVAndLoadModel
import com.growatt.atess.model.plant.SynopsisTotalModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.atess.view.DateType
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.util.DateUtils
import kotlinx.coroutines.launch
import java.util.*

/**
 * 首页-总览
 */
class HomeSynopsisViewModel : BaseViewModel() {

    var selectDate = Date()
    var dateType = DateType.HOUR //dateType 1 —— 时；2 —— 日；3 —— 月；4 —— 年

    val getSynopsisTotalLiveData = MutableLiveData<Pair<SynopsisTotalModel?, String?>>()

    val getPowerTrendsChartLiveData = MutableLiveData<Pair<ChartListDataModel?, String?>>()

    val getPVAndLoadLiveData = MutableLiveData<Pair<PVAndLoadModel?, String?>>()

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

    /**
     * 获取电量趋势图表详情
     */
    fun getPowerTrendsInfo() {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("queryDate", DateUtils.yyyy_MM_dd_format(selectDate))
                put("type", dateType)
            }
            apiService().postForm(ApiPath.Plant.GET_POWER_TRENDS_INFO, params, object :
                HttpCallback<HttpResult<ChartListDataModel>>() {
                override fun success(result: HttpResult<ChartListDataModel>) {
                    if (result.isBusinessSuccess()) {
                        getPowerTrendsChartLiveData.value = Pair(result.data, null)
                    } else {
                        getPowerTrendsChartLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getPowerTrendsChartLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }

    /**
     * 获取光伏产出与负载用电
     */
    fun getPVAndLoadInfo() {
        viewModelScope.launch {
            apiService().post(ApiPath.Plant.GET_PV_AND_LOAD, object :
                HttpCallback<HttpResult<PVAndLoadModel>>() {
                override fun success(result: HttpResult<PVAndLoadModel>) {
                    if (result.isBusinessSuccess()) {
                        getPVAndLoadLiveData.value = Pair(result.data, null)
                    } else {
                        getPVAndLoadLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getPVAndLoadLiveData.value = Pair(null, error ?: "")
                }
            })
        }
    }
}