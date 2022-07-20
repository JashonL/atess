package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityMyDeviceListBinding
import com.growatt.atess.ui.plant.viewmodel.DeviceListViewModel

/**
 * 我的设备
 */
class MyDeviceListActivity : BaseActivity(), View.OnClickListener {

    companion object {
        private const val KEY_PLANT_ID = "key_plant_id"

        fun start(plantId: String, context: Context?) {
            context?.startActivity(Intent(context, MyDeviceListActivity::class.java).also {
                it.putExtra(KEY_PLANT_ID, plantId)
            })
        }

    }

    private lateinit var binding: ActivityMyDeviceListBinding

    private val viewModel: DeviceListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDeviceListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        viewModel.plantId = intent.getStringExtra(KEY_PLANT_ID) ?: ""
    }

    private fun setListener() {
        binding.tvSearch.setOnClickListener(this)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvSearch -> {}
        }
    }

}