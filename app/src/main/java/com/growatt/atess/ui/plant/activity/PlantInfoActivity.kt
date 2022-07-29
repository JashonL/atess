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
import com.growatt.atess.ui.plant.fragment.info.PcsSystemOperationFragment
import com.growatt.atess.ui.plant.monitor.PlantMonitor
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.atess.view.dialog.OptionsDialog
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

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
        viewModel.getPlantInfoLiveData.observe(this) {
            if (it.second == null) {
                binding.title.setTitleText(it.first?.plantName)
                if (it.first?.hasDevices() == true) {
                    binding.llHpsPcsInfo.visible()
                    viewModel.getPcsHpsSN()
                } else {
                    binding.llHpsPcsInfo.gone()
                }
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getPcsHpsSNLiveData.observe(this) {
            if (it.third == null) {
                handlePcsHpsSNList(it.first, it.second)
            } else {
                ToastUtil.show(it.third)
            }
        }
        refresh()
    }

    /**
     * @param snList hps与pcs列表
     * @param isDataChange 数据是否有改变
     */
    private fun handlePcsHpsSNList(snList: MutableList<Pair<Int, String>>?, isDataChange: Boolean) {
        if (snList?.isNullOrEmpty() == true) {
            binding.llHpsPcsInfo.gone()
        } else {
            binding.llHpsPcsInfo.visible()
            if (isDataChange) {
                val defaultDevice = snList[0]
                viewModel.typeAndSn = defaultDevice
                refreshSNView(defaultDevice)
            }
            refreshDeviceInfo()
        }
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
            refresh()
        }
        binding.srlRefresh.setOnRefreshListener {
            refresh()
            binding.srlRefresh.finishRefresh(2000)
        }
        binding.title.setOnTitleClickListener {
            if (viewModel.plantModels?.size ?: 0 > 1) {
                showPlantList(viewModel.plantModels!!)
            }
        }
    }

    private fun showPlantList(plantModels: Array<PlantModel>) {
        val options = mutableListOf<String>()
        for (plant in plantModels) {
            options.add(plant.plantName ?: "")
        }
        OptionsDialog.show(supportFragmentManager, options.toTypedArray()) {
            val plant = plantModels[it]
            if (plant.id != viewModel.plantId) {
                viewModel.plantId = plant.id
                refresh()
            }
        }
    }

    /**
     * 先请求电站详情与设备列表，再根据电站详情请求设备相关的信息
     */
    private fun refresh() {
        viewModel.getPlantInfo()
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
        when (viewModel.typeAndSn?.first) {
            DeviceType.HPS -> {
                if (systemOperationFragment is HpsSystemOperationFragment) {
                    systemOperationFragment.refresh()
                } else {
                    supportFragmentManager.commit(true) {
                        replace(
                            R.id.fragment_system_operation,
                            HpsSystemOperationFragment(
                                viewModel.plantId,
                                viewModel.typeAndSn?.second
                            )
                        )
                    }
                }
            }
            DeviceType.PCS -> {
                if (systemOperationFragment is PcsSystemOperationFragment) {
                    systemOperationFragment.refresh()
                } else {
                    supportFragmentManager.commit(true) {
                        replace(
                            R.id.fragment_system_operation,
                            PcsSystemOperationFragment(
                                viewModel.plantId,
                                viewModel.typeAndSn?.second
                            )
                        )
                    }
                }
            }
        }
    }

}