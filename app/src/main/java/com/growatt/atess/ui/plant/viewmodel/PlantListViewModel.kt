package com.growatt.atess.ui.plant.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.PlantListResultModel
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.model.plant.PlantStatusNumModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 电站列表
 */
class PlantListViewModel : BaseViewModel() {

    val getPlantListLiveData = MutableLiveData<Pair<Array<PlantModel>?, String?>>()

    val getPlantStatusNumLiveData = MutableLiveData<Pair<PlantStatusNumModel, String?>>()

    val deletePlantLiveData = MutableLiveData<String?>()

    /**
     * 获取电站列表
     */
    fun getPlantList(plantStatus: Int, orderType: Int? = null, searchWord: String = "") {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                if (plantStatus != PlantModel.PLANT_STATUS_ALL) {
                    put("plantStatus", plantStatus.toString())
                }
                if (orderType != null) {
                    put("order", orderType.toString())
                }
                if (!TextUtils.isEmpty(searchWord)) {
                    put("searchWord", searchWord)
                }
            }
            apiService().postForm(
                ApiPath.Plant.GET_PLANT_LIST,
                params,
                object : HttpCallback<HttpResult<PlantListResultModel>>() {
                    override fun success(result: HttpResult<PlantListResultModel>) {
                        if (result.isBusinessSuccess()) {
                            getPlantListLiveData.value =
                                Pair(result.data?.plantList ?: emptyArray(), null)
                            getPlantStatusNumLiveData.value =
                                Pair(result.data?.statusMap ?: PlantStatusNumModel(), null)
                        } else {
                            getPlantListLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        getPlantListLiveData.value = Pair(null, error ?: "")
                    }
                })
        }
    }


    /**
     * 删除电站
     */
    fun deletePlant(plantId: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("plantId", plantId)
            }
            apiService().postForm(
                ApiPath.Plant.DELETE_PLANT,
                params,
                object : HttpCallback<HttpResult<String?>>() {
                    override fun success(result: HttpResult<String?>) {
                        if (result.isBusinessSuccess()) {
                            deletePlantLiveData.value = null
                        } else {
                            deletePlantLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        deletePlantLiveData.value = error ?: ""
                    }
                })
        }
    }


}