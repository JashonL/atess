package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityPlantDeviceListBinding
import com.growatt.atess.ui.plant.fragment.DeviceTabFragment

/**
 * 我的设备（电站设备列表）
 */
class PlantDeviceListActivity : BaseActivity(), View.OnClickListener {

    companion object {
        private const val KEY_PLANT_ID = "key_plant_id"

        fun start(plantId: String?, context: Context?) {
            context?.startActivity(Intent(context, PlantDeviceListActivity::class.java).also {
                it.putExtra(KEY_PLANT_ID, plantId)
            })
        }

    }

    private lateinit var binding: ActivityPlantDeviceListBinding
    private lateinit var plantId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantDeviceListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        plantId = intent.getStringExtra(KEY_PLANT_ID) ?: ""
    }

    private fun setListener() {
        binding.tvSearch.setOnClickListener(this)
    }

    private fun initView() {
        supportFragmentManager.commit(true) {
            add(R.id.fragment_device_tab, DeviceTabFragment(plantId))
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvSearch -> SearchActivity.startDeviceSearch(this, plantId)
        }
    }

}