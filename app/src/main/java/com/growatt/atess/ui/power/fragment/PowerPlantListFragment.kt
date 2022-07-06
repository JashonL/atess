package com.growatt.atess.ui.power.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPowerPlantListBinding

/**
 * 电站列表
 */
class PowerPlantListFragment : BaseFragment() {

    private lateinit var binding: FragmentPowerPlantListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPowerPlantListBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {

    }

}