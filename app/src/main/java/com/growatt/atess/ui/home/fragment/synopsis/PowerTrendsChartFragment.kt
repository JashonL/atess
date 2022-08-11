package com.growatt.atess.ui.home.fragment.synopsis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPowerTrendsChartBinding
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.ui.home.viewmodel.HomeSynopsisViewModel
import com.growatt.atess.ui.plant.fragment.BarChartFragment
import com.growatt.atess.ui.plant.fragment.LineChartFragment
import com.growatt.atess.view.DateType
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 电量趋势图表
 */
class PowerTrendsChartFragment : BaseFragment() {

    private var _binding: FragmentPowerTrendsChartBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeSynopsisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPowerTrendsChartBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getPowerTrendsChartLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                showData(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    fun refresh() {
        viewModel.getPowerTrendsInfo()
    }

    private fun showData(chartListDataModel: ChartListDataModel?) {
        binding.root.visible()
        chartListDataModel?.also {
            val chartFragment = childFragmentManager.findFragmentById(R.id.fragment_chart)
            if (isShowLineChart()) {
                val kw = MainApplication.instance().getString(R.string.kw)
                if (chartFragment is LineChartFragment) {
                    chartFragment.refresh(chartListDataModel, kw)
                } else {
                    childFragmentManager.commit(true) {
                        replace(
                            R.id.fragment_chart,
                            LineChartFragment(chartListDataModel, kw)
                        )
                    }
                }
            } else {
                val kwh = MainApplication.instance().getString(R.string.kwh)
                if (chartFragment is BarChartFragment) {
                    chartFragment.refresh(chartListDataModel, kwh)
                } else {
                    childFragmentManager.commit(true) {
                        replace(
                            R.id.fragment_chart,
                            BarChartFragment(chartListDataModel, kwh)
                        )
                    }
                }
            }
        }
    }

    private fun isShowLineChart(): Boolean {
        return viewModel.dateType == DateType.HOUR
    }

    private fun setListener() {
        binding.chartTimeSelectLayout.setChartTimeSelectListener { dateType, date ->
            viewModel.dateType = dateType
            viewModel.selectDate = date
            showDialog()
            viewModel.getPowerTrendsInfo()
        }
    }

    private fun initView() {
        binding.root.gone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}