package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantTotalBinding

/**
 * 电站详情-总计（头部）
 */
class PlantTotalFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentPlantTotalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantTotalBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
    }

    private fun setListener() {
        binding.tvCity.setOnClickListener(this)
    }

    private fun initView() {
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvCity -> {}
        }
    }

}