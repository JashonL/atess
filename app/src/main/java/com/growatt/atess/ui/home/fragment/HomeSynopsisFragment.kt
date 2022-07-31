package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.growatt.atess.R
import com.growatt.atess.databinding.FragmentHomeSynopsisBinding
import com.growatt.atess.ui.home.HomeActivity
import com.growatt.atess.ui.home.fragment.synopsis.PowerTrendsChartFragment
import com.growatt.atess.ui.home.view.HomeTab
import com.growatt.atess.ui.home.viewmodel.HomeSynopsisViewModel
import com.growatt.atess.ui.plant.activity.AddPlantActivity
import com.growatt.atess.ui.plant.monitor.PlantTabSwitchMonitor
import com.growatt.lib.util.ToastUtil

/**
 * 首页-总览
 */
class HomeSynopsisFragment : HomeBaseFragment(), View.OnClickListener {

    private var _binding: FragmentHomeSynopsisBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeSynopsisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeSynopsisBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getSynopsisTotalLiveData.observe(viewLifecycleOwner) {
            binding.srlRefresh.finishRefresh()
            if (it.second == null) {
                binding.total = it.first
            } else {
                ToastUtil.show(it.second)
            }
        }
        refresh()
    }

    private fun initView() {
        binding.title.setOnRightImageClickListener {
            AddPlantActivity.start(requireContext())
        }
    }

    fun refresh() {
        binding.srlRefresh.autoRefresh()
    }

    fun setListener() {
        binding.srlRefresh.setOnRefreshListener {
            viewModel.getSynopsisTotal()
            val powerTrendsChartFragment =
                childFragmentManager.findFragmentById(R.id.fragment_power_trends_chart)
            if (powerTrendsChartFragment is PowerTrendsChartFragment) {
                powerTrendsChartFragment.refresh()
            }
        }
        binding.llTotalComponentPower.setOnClickListener(this)
        binding.llPlantCount.setOnClickListener(this)
        binding.llWarningMessage.setOnClickListener(this)
        binding.llOnlineDevice.setOnClickListener(this)
        binding.llOfflineDevice.setOnClickListener(this)
        binding.llTroubleDevice.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when {
            v === binding.llTotalComponentPower -> jumpToHomePlant(0)
            v === binding.llPlantCount -> jumpToHomePlant(0)
            v === binding.llWarningMessage -> {}
            v === binding.llOnlineDevice -> jumpToHomePlant(1)
            v === binding.llOfflineDevice -> jumpToHomePlant(2)
            v === binding.llTroubleDevice -> jumpToHomePlant(3)
        }
    }

    private fun jumpToHomePlant(position: Int) {
        val activity = requireActivity()
        if (activity is HomeActivity) {
            activity.setHomeBottomPosition(HomeTab.PLANT)
            PlantTabSwitchMonitor.notifyPlantSwitch(position)
        }
    }
}