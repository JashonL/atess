package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantDeviceListBinding
import com.growatt.atess.ui.plant.activity.PlantDeviceListActivity
import com.growatt.atess.ui.plant.adapter.DeviceAdapter
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.atess.view.itemdecoration.DividerItemDecoration
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 电站详情-我的设备
 */
class PlantDeviceListFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentPlantDeviceListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantDeviceListBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getDeviceListLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                if (it.first.isNullOrEmpty()) {
                    binding.root.gone()
                } else {
                    binding.root.visible()
                }
                (binding.rvDeviceList.adapter as DeviceAdapter).refresh(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun setListener() {
        binding.tvSeeMore.setOnClickListener(this)
    }

    private fun initView() {
        binding.rvDeviceList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.color_line)
            )
        )
        binding.rvDeviceList.adapter = DeviceAdapter()
        binding.root.gone()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvSeeMore -> viewModel.plantId?.let {
                PlantDeviceListActivity.start(
                    it,
                    requireContext()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}