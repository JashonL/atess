package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantTabBinding
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.model.plant.PlantStatusNumModel
import com.growatt.atess.ui.plant.monitor.PlantTabSwitchMonitor

/**
 * 电站列表TAB
 */
class PlantTabFragment(val searchWord: String = "") : BaseFragment(),
    OnPlantStatusNumChangeListener {

    private var _binding: FragmentPlantTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantTabBinding.inflate(inflater, container, false)
        initView()
        setListener()
        return binding.root
    }

    private fun refreshPlantStatusNum(plantStatusNumModel: PlantStatusNumModel) {
        binding.tabLayout.setTabText(getString(R.string.all_format, plantStatusNumModel.allNum), 0)
        binding.tabLayout.setTabText(
            getString(
                R.string.online_format,
                plantStatusNumModel.onlineNum
            ), 1
        )
        binding.tabLayout.setTabText(
            getString(
                R.string.offline_format,
                plantStatusNumModel.offline
            ), 2
        )
        binding.tabLayout.setTabText(
            getString(
                R.string.trouble_format,
                plantStatusNumModel.faultNum
            ), 3
        )
    }

    private fun initView() {
        binding.tabLayout.setSelectTabPosition(0, false)
        binding.vpPlant.adapter = Adapter(this)
        binding.tabLayout.setupWithViewPager2(binding.vpPlant)
        binding.vpPlant.offscreenPageLimit = binding.vpPlant.childCount

        refreshPlantStatusNum(PlantStatusNumModel())
    }

    private fun setListener() {
        PlantTabSwitchMonitor.watch(viewLifecycleOwner.lifecycle) { _, position ->
            binding.tabLayout.setSelectTabPosition(position, false)
        }
    }

    inner class Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragments = arrayOf(
            PlantListFragment(PlantModel.PLANT_STATUS_ALL, this@PlantTabFragment, searchWord),
            PlantListFragment(PlantModel.PLANT_STATUS_ONLINE, this@PlantTabFragment, searchWord),
            PlantListFragment(PlantModel.PLANT_STATUS_OFFLINE, this@PlantTabFragment, searchWord),
            PlantListFragment(PlantModel.PLANT_STATUS_TROUBLE, this@PlantTabFragment, searchWord)
        )

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onPlantStatusNumChange(plantStatusNumModel: PlantStatusNumModel) {
        refreshPlantStatusNum(plantStatusNumModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

interface OnPlantStatusNumChangeListener {
    fun onPlantStatusNumChange(plantStatusNumModel: PlantStatusNumModel)
}