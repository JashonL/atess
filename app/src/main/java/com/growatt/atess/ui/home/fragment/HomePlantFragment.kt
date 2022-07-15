package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.growatt.atess.databinding.FragmentHomePlantBinding
import com.growatt.atess.model.plant.PlantFilterModel
import com.growatt.atess.ui.home.viewmodel.PlantFilterViewModel
import com.growatt.atess.ui.plant.view.PlantFilterPopup

/**
 * 首页-电站
 */
class HomePlantFragment : HomeBaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomePlantBinding
    private var selectedFilterModer: PlantFilterModel =
        PlantFilterModel.getDefaultFilter()
    private val filterViewModel: PlantFilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePlantBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        filterViewModel.setFilterType(PlantFilterModel.getDefaultFilter().filterType)
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
                PlantFilterPopup.show(
                    requireContext(),
                    parent,
                    width,
                    height,
                    selectedFilterModer
                ) {
                    selectedFilterModer = it
                    filterViewModel.setFilterType(it.filterType)
                    binding.tvFilter.text = it.filterName
                }
            }
            v === binding.tvSearch -> {}
        }
    }

}