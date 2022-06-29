package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.databinding.FragmentHomeServiceBinding

/**
 * 首页-服务
 */
class HomeServiceFragment : HomeBaseFragment() {

    private lateinit var binding: FragmentHomeServiceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

}