package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantListBinding

/**
 * 电站列表
 */
class PlantListFragment : BaseFragment() {

    private lateinit var binding: FragmentPlantListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantListBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {

    }

}