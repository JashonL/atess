package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityAddPlantBinding
import com.growatt.atess.ui.plant.fragment.AddPlant1Fragment
import com.growatt.atess.ui.plant.fragment.AddPlant2Fragment
import com.growatt.atess.ui.plant.viewmodel.AddPlantViewModel

/**
 * 添加电站页面
 * @see com.growatt.atess.ui.plant.fragment.AddPlant1Fragment
 * @see com.growatt.atess.ui.plant.fragment.AddPlant2Fragment
 */
class AddPlantActivity : BaseActivity(), View.OnClickListener {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, AddPlantActivity::class.java))
        }

    }

    private lateinit var addPlant1Fragment: AddPlant1Fragment
    private lateinit var addPlant2Fragment: AddPlant2Fragment

    private lateinit var binding: ActivityAddPlantBinding

    private val viewModel: AddPlantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {

    }

    private fun setListener() {
        binding.btNextStep.setOnClickListener(this)
    }

    private fun initView() {
        if (viewModel.isEditMode) {
            binding.title.setTitleText(getString(R.string.edit_plant))
        } else {
            binding.title.setTitleText(getString(R.string.add_plant))
        }

        addPlant1Fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_add_plant_1) as AddPlant1Fragment
        addPlant2Fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_add_plant_2) as AddPlant2Fragment
    }

    override fun onClick(v: View?) {
        when {
            v === binding.btNextStep -> {

            }
        }
    }
}