package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantEnergyInfoBinding

/**
 * 能源概况
 */
class PlantEnergyInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentPlantEnergyInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantEnergyInfoBinding.inflate(inflater, container, false)
        initData()
        initView()
        return binding.root
    }

    private fun initData() {

    }

    private fun initView() {

    }

}