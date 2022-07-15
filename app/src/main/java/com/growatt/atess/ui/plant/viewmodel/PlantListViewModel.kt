package com.growatt.atess.ui.plant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.model.plant.PlantResultModel
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

    /**
     * 获取电站列表
     */
    fun getPlantList(plantStatus: Int, orderType: Int = 1) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                if (plantStatus != PlantModel.PLANT_STATUS_ALL) {
                    put("plantStatus", plantStatus.toString())
                }
                put("order", orderType.toString())
            }
            apiService().postForm(
                ApiPath.Plant.GET_PLANT_LIST,
                params,
                object : HttpCallback<HttpResult<PlantResultModel>>() {
                    override fun success(result: HttpResult<PlantResultModel>) {
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
}