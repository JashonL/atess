package com.growatt.atess.ui.plant.viewmodel

import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.AddPlantModel

/**
 * 添加电站
 */
class AddPlantViewModel : BaseViewModel() {

    var isEditMode: Boolean = false

    val addPlantModel = AddPlantModel()

}