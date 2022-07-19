package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityPlantInfoBinding
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel

/**
 * 电站详情页面
 */
class PlantInfoActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_PLANT_ID = "key_plant_id"
        private const val KEY_PLANT_LIST = "key_plant_list"

        fun start(
            context: Context?,
            plantId: String?,
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
        initView()
        setListener()
    }

    private fun initView() {
        val plantCount = viewModel.plantModels?.size ?: 0
        if (plantCount > 1) {
            binding.title.setFilterIconVisible(true)
        } else {
            binding.title.setFilterIconVisible(false)
        }
    }

    private fun initData() {
        viewModel.plantId = intent.getStringExtra(KEY_PLANT_ID)
        viewModel.plantModels =
            intent.getParcelableArrayExtra(KEY_PLANT_LIST) as? Array<PlantModel>?
    }

    private fun setListener() {
        binding.tvDeviceSn.setOnClickListener(this)
        binding.title.setOnRightImageClickListener {
            AddCollectorActivity.start(this, viewModel.plantId)
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvDeviceSn -> {

            }
        }
    }

}