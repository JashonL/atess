package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.databinding.FragmentHomePowerPlantBinding
import com.growatt.atess.model.power.PowerPlantFilterModel
import com.growatt.atess.ui.power.view.PowerPlantFilterPopup

/**
 * 首页-电站
 */
class HomePowerPlantFragment : HomeBaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomePowerPlantBinding
    private var selectedFilterModer: PowerPlantFilterModel =
        PowerPlantFilterModel.getDefaultFilter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePowerPlantBinding.inflate(inflater, container, false)
        initView()
        setListener()
        return binding.root
    }

    private fun setListener() {
        binding.tvFilter.setOnClickListener(this)
        binding.tvSearch.setOnClickListener(this)
    }

    private fun initView() {
        binding.tvFilter.text = selectedFilterModer.filterName
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvFilter -> {
                val parent = v.parent as View
                val width = v.width
                val height = deviceService().getDeviceHeight() - parent.bottom
                PowerPlantFilterPopup.show(
                    requireContext(),
                    parent,
                    width,
                    height,
                    selectedFilterModer
                ) {
                    selectedFilterModer = it
                    binding.tvFilter.text = it.filterName
                }
            }
            v === binding.tvSearch -> {}
        }
    }

}