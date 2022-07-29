package com.growatt.atess.ui.service.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentInstallVideoBinding

/**
 * 安装视频
 */
class InstallVideoFragment : BaseFragment() {

    private var _binding: FragmentInstallVideoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstallVideoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}