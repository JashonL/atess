package com.growatt.atess.ui.service.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentServiceManualBinding

/**
 * 使用手册
 */
class ServiceManualFragment : BaseFragment() {

    private lateinit var binding: FragmentServiceManualBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServiceManualBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}