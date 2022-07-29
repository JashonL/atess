package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.databinding.FragmentHomeSynopsisBinding
import com.growatt.atess.ui.plant.activity.AddPlantActivity

/**
 * 首页-总览
 */
class HomeSynopsisFragment : HomeBaseFragment() {

    private var _binding: FragmentHomeSynopsisBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeSynopsisBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.title.setOnRightImageClickListener {
            AddPlantActivity.start(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}