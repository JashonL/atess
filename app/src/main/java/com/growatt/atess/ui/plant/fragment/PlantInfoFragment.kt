package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantInfoBinding
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.plant.activity.AddCollectorActivity
import com.growatt.atess.ui.plant.monitor.PlantMonitor
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.atess.view.dialog.OptionsDialog
import com.growatt.lib.util.ToastUtil

/**
 * 电站详情页面
 */
class PlantInfoFragment(private val onBackPlantList: (() -> Unit)? = null) : BaseFragment() {

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
                    viewModel.getPcsHpsSN()
                }
            } else {
                ToastUtil.show(it.second)
            }
        }
        refresh()
    }

    private fun setListener() {
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

}