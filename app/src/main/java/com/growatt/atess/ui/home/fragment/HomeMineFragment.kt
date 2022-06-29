package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.databinding.FragmentHomeMineBinding

/**
 * 首页-我的
 */
class HomeMineFragment : HomeBaseFragment() {

    private lateinit var binding: FragmentHomeMineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMineBinding.inflate(inflater, container, false)
        return binding.root
    }

}