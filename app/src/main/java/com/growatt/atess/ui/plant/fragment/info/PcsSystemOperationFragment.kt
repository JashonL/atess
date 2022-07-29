package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPcsSystemOperationBinding
import com.growatt.atess.ui.plant.activity.MyDeviceListActivity

/**
 * PCS设备系统运行图
 */
class PcsSystemOperationFragment(val plantId: String?, val deviceSn: String?) : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentPcsSystemOperationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPcsSystemOperationBinding.inflate(inflater, container, false)
        initData()
        initView()
        return binding.root
    }

    private fun initData() {

    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivPcs -> if (plantId != null) MyDeviceListActivity.start(
                plantId,
                requireActivity()
            )
        }
    }

}