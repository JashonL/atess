package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityPlantInfoBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.plant.fragment.info.HpsSystemOperationFragment
import com.growatt.atess.ui.plant.monitor.PlantMonitor
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.atess.view.dialog.OptionsDialog
import com.growatt.lib.util.ToastUtil

/**
 * 电站详情页面
 */
class PlantInfoActivity : BaseActivity(), View.OnClickListener {

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
        val parcelableArrayExtra = intent.getParcelableArrayExtra(KEY_PLANT_LIST)
        if (!parcelableArrayExtra.isNullOrEmpty()) {
            viewModel.plantModels = Array(parcelableArrayExtra.size) {
                PlantModel()
            }
            for (index in parcelableArrayExtra.indices) {
                viewModel.plantModels!![index] = parcelableArrayExtra[index] as PlantModel
            }
        }

        viewModel.getPcsHpsSNLiveData.observe(this) {
            if (it.second == null) {
                if (viewModel.typeAndSn == null && it.first?.isNullOrEmpty() == false) {
                    val defaultDevice = it.first!![0]
                    refreshSNView(defaultDevice)
                    viewModel.typeAndSn = defaultDevice
                    viewModel.getEnergyInfo()
                    viewModel.getChartInfo()
                    if (defaultDevice.first == DeviceType.HPS) {
                        supportFragmentManager.commit(true) {
                            replace(
                                R.id.fragment_system_operation,
                                HpsSystemOperationFragment(viewModel.plantId, defaultDevice.second)
                            )
                        }
                    } else if (defaultDevice.first == DeviceType.PCS) {

                    }
                }
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getPlantInfo()
        viewModel.getPcsHpsSN()
        viewModel.getDeviceList()
    }

    private fun refreshSNView(typeAndSN: Pair<Int, String>) {
        binding.tvDeviceType.text = MainApplication.instance()
            .getString(if (typeAndSN.first == DeviceType.HPS) R.string.hps else R.string.pcs)
        binding.tvDeviceSn.text = typeAndSN.second
    }

    private fun setListener() {
        binding.tvDeviceSn.setOnClickListener(this)
        binding.title.setOnRightImageClickListener {
            AddCollectorActivity.start(this, viewModel.plantId)
        }
        PlantMonitor.watch(lifecycle) {

        }
        binding.srlRefresh.setOnRefreshListener {
            refresh()
            binding.srlRefresh.finishRefresh(2000)
        }
    }

    private fun refresh() {
        viewModel.getPlantInfo()
        refreshDeviceInfo()
        viewModel.getDeviceList()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvDeviceSn -> selectSN()
        }
    }

    private fun selectSN() {
        val options = mutableListOf<String>()
        val snList = viewModel.getPcsHpsSNLiveData.value?.first
        snList?.all {
            options.add(it.second)
            true
        }
        if (options.size > 0) {
            OptionsDialog.show(supportFragmentManager, options.toTypedArray()) {
                val selectDevice = snList!![it]
                refreshSNView(selectDevice)
                viewModel.typeAndSn = selectDevice
                refreshDeviceInfo()
            }
        }
    }

    private fun refreshDeviceInfo() {
        viewModel.getEnergyInfo()
        viewModel.getChartInfo()
        val systemOperationFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_system_operation)
        if (systemOperationFragment is HpsSystemOperationFragment) {
            systemOperationFragment.refresh()
        }
    }

}