package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.databinding.FragmentHomeSynopsisBinding

/**
 * 首页-总览
 */
class HomeSynopsisFragment : HomeBaseFragment() {

    private lateinit var binding: FragmentHomeSynopsisBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeSynopsisBinding.inflate(inflater, container, false)
        return binding.root
    }

}