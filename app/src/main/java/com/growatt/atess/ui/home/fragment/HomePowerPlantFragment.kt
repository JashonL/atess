package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.databinding.FragmentHomePowerPlantBinding

/**
 * 首页-电站
 */
class HomePowerPlantFragment : HomeBaseFragment() {

    private lateinit var binding: FragmentHomePowerPlantBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePowerPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

}