package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.growatt.atess.R
import com.growatt.atess.databinding.FragmentHomePlantBinding
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.plant.fragment.PlantInfoFragment
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.atess.ui.plant.viewmodel.PlantListViewModel
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 首页-电站
 */
class HomePlantFragment : HomeBaseFragment() {

    private var _binding: FragmentHomePlantBinding? = null

    private val binding get() = _binding!!
    private val viewModel: PlantListViewModel by viewModels()
    private val plantInfoViewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePlantBinding.inflate(inflater, container, false)
        initData()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getPlantListLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                binding.errorPage.root.gone()
                childFragmentManager.commit(true) {
                    if (it.first?.size ?: 0 == 1) {
                        plantInfoViewModel.plantId = it.first?.get(0)?.id
                        replace(R.id.fragment_home_plant, PlantInfoFragment {
                            childFragmentManager.commit(true) {
                                replace(R.id.fragment_home_plant, HomePlantListFragment())
                            }
                        })
                    } else {
                        add(R.id.fragment_home_plant, HomePlantListFragment())
                    }
                }
            } else {
                binding.errorPage.root.visible()
            }
        }

        fetchPlantList()
    }

    fun setListener() {
        binding.errorPage.root.setOnClickListener {
            fetchPlantList()
        }
    }

    private fun fetchPlantList() {
        showDialog()
        viewModel.getPlantList(PlantModel.PLANT_STATUS_ALL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}