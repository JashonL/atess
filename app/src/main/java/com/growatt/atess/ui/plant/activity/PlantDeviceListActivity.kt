package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityPlantDeviceListBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.ui.plant.fragment.DeviceTabFragment
import com.growatt.lib.util.ToastUtil

/**
 * 我的设备（电站设备列表）
 */
class PlantDeviceListActivity : BaseActivity(), View.OnClickListener {

    companion object {
        private const val KEY_PLANT_ID = "key_plant_id"

        fun start(plantId: String?, context: Context?) {
            if (MainApplication.instance().accountService().isGuest()) {
                ToastUtil.show(
                    MainApplication.instance().getString(R.string.info_space_not_permission)
                )
            } else {
                context?.startActivity(Intent(context, PlantDeviceListActivity::class.java).also {
                    it.putExtra(KEY_PLANT_ID, plantId)
                })
            }
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
        binding.title.setOnRightImageClickListener {
            AddCollectorActivity.start(this, plantId)
        }
    }

    private fun initView() {
        supportFragmentManager.commit(true) {
            add(R.id.fragment_device_tab, DeviceTabFragment(plantId) {
                binding.title.setRightImageVisible(it == DeviceType.COLLECTOR)
            })
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvSearch -> SearchActivity.startDeviceSearch(this, plantId)
        }
    }

}