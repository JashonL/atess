package com.growatt.atess.ui.plant.viewmodel

import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.PlantModel

/**
 * 电站详情
 */
class PlantInfoViewModel : BaseViewModel() {

    var plantId: String? = null
    var plantModels: Array<PlantModel>? = null

}