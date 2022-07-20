package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantDeviceListBinding
import com.growatt.atess.ui.plant.adapter.DeviceAdapter
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.lib.util.ToastUtil

/**
 * 电站详情-我的设备
 */
class PlantDeviceListFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentPlantDeviceListBinding

    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantDeviceListBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getDeviceListLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                (binding.rvDeviceList.adapter as DeviceAdapter).refresh(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getDeviceList()
    }

    private fun setListener() {
        binding.tvSeeMore.setOnClickListener(this)
    }

    private fun initView() {
        binding.rvDeviceList.adapter = DeviceAdapter()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvSeeMore -> {}
        }
    }

}