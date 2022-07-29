package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantEmptyDeviceBinding
import com.growatt.atess.ui.plant.activity.AddCollectorActivity
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 电站详情-无设备页面
 */
class PlantEmptyDeviceFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentPlantEmptyDeviceBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantEmptyDeviceBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getPlantInfoLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                if (it.first?.hasDevices() == true) {
                    binding.root.gone()
                } else {
                    binding.root.visible()
                }
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun setListener() {
        binding.root.setOnClickListener(this)
    }

    private fun initView() {
        binding.root.gone()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.root -> {
                AddCollectorActivity.start(requireContext(), viewModel.plantId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}