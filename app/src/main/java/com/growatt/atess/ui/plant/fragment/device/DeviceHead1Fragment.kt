package com.growatt.atess.ui.plant.fragment.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentDeviceHead1Binding
import com.growatt.atess.model.plant.ui.IDeviceInfoHead
import com.growatt.atess.ui.plant.activity.DeviceParamsActivity

/**
 * 设备详情-HPS、PCS、PBD头部
 */
class DeviceHead1Fragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentDeviceHead1Binding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceHead1Binding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.tvAllParams.setOnClickListener(this)
    }

    fun bindData(data: IDeviceInfoHead) {
        binding.tvDeviceSn.text = data.getIDeviceSn()
        binding.tvDeviceModel.text = data.getIDeviceModel()
        binding.tvAllParams.tag = data.getIDeviceParamsJsonStr()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvAllParams -> DeviceParamsActivity.start(
                requireActivity(),
                v.tag as String
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}