package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantInfoBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.plant.activity.AddCollectorActivity
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
class PlantInfoFragment(private val onBackPlantList: (() -> Unit)? = null) : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentPlantInfoBinding
    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantInfoBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
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
        viewModel.getPlantInfoLiveData.observe(viewLifecycleOwner) {
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
        viewModel.getPcsHpsSNLiveData.observe(viewLifecycleOwner) {
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
            AddCollectorActivity.start(requireActivity(), viewModel.plantId)
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
        binding.title.setOnLeftIconClickListener {
            if (onBackPlantList == null) {
                activity?.onBackPressed()
            } else {
                onBackPlantList.invoke()
            }
        }
    }

    private fun showPlantList(plantModels: Array<PlantModel>) {
        val options = mutableListOf<String>()
        for (plant in plantModels) {
            options.add(plant.plantName ?: "")
        }
        OptionsDialog.show(childFragmentManager, options.toTypedArray()) {
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
            OptionsDialog.show(childFragmentManager, options.toTypedArray()) {
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
            childFragmentManager.findFragmentById(R.id.fragment_system_operation)
        when (viewModel.typeAndSn?.first) {
            DeviceType.HPS -> {
                if (systemOperationFragment is HpsSystemOperationFragment) {
                    systemOperationFragment.refresh()
                } else {
                    childFragmentManager.commit(true) {
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
                    childFragmentManager.commit(true) {
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