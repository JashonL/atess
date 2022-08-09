package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityPlantInfoBinding
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel

/**
 * 电站详情页面
 */
class PlantInfoActivity : BaseActivity() {

    companion object {

        private const val KEY_PLANT_ID = "key_plant_id"
        private const val KEY_PLANT_LIST = "key_plant_list"

        fun start(
            context: Context?,
            plantId: String,
            plantModels: Array<PlantModel> = emptyArray()
        ) {
            context?.startActivity(Intent(context, PlantInfoActivity::class.java).also {
                it.putExtra(KEY_PLANT_ID, plantId)
                it.putExtra(KEY_PLANT_LIST, plantModels)
            })
        }

    }

    private lateinit var binding: ActivityPlantInfoBinding
    private val viewModel: PlantInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData() {
        viewModel.plantId = intent.getStringExtra(KEY_PLANT_ID)
        val parcelableArrayExtra = intent.getParcelableArrayExtra(KEY_PLANT_LIST)
        if (!parcelableArrayExtra.isNullOrEmpty()) {
            viewModel.plantModels = Array(parcelableArrayExtra.size) {
                PlantModel()
            }
            for (index in parcelableArrayExtra.indices) {
                viewModel.plantModels!![index] = parcelableArrayExtra[index] as PlantModel
            }
        }
    }

}