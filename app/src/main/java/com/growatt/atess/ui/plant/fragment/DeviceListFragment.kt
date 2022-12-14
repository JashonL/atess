package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentDeviceListBinding
import com.growatt.atess.model.plant.DeviceModel
import com.growatt.atess.ui.plant.adapter.DeviceAdapter
import com.growatt.atess.view.itemdecoration.DividerItemDecoration

/**
 * 我的设备-设备列表
 */
class DeviceListFragment(private val deviceList: Array<DeviceModel>?) : BaseFragment() {

    private var _binding: FragmentDeviceListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceListBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.rvDeviceList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                resources.getColor(android.R.color.transparent),
                10f
            )
        )
        binding.rvDeviceList.adapter = DeviceAdapter().also {
            it.refresh(deviceList?.toList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}