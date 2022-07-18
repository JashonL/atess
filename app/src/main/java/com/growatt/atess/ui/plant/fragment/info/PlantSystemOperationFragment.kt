package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantSystemOperationBinding

/**
 * 系统运行图
 */
class PlantSystemOperationFragment : BaseFragment() {

    private lateinit var binding: FragmentPlantSystemOperationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantSystemOperationBinding.inflate(inflater, container, false)
        initData()
        initView()
        return binding.root
    }

    private fun initData() {

    }

    private fun initView() {

    }

}