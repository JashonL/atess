package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPcsSystemOperationBinding
import com.growatt.atess.ui.plant.activity.PlantDeviceListActivity

/**
 * PCS设备系统运行图
 */
class PcsSystemOperationFragment(val plantId: String?, val deviceSn: String?) : BaseFragment(),
    View.OnClickListener {

    private var _binding: FragmentPcsSystemOperationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPcsSystemOperationBinding.inflate(inflater, container, false)
        initData()
        initView()
        return binding.root
    }

    private fun initData() {

    }

    private fun initView() {

    }

    fun refresh() {

    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivPcs -> if (plantId != null) PlantDeviceListActivity.start(
                plantId,
                requireActivity()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}