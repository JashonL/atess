package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantTabChartBinding
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.model.plant.PlantInfoResultModel
import com.growatt.atess.ui.plant.fragment.BarChartFragment
import com.growatt.atess.ui.plant.fragment.LineChartFragment
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.atess.view.DateType
import com.growatt.atess.view.OnTabSelectedListener
import com.growatt.atess.view.Tab
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.invisible
import com.growatt.lib.util.visible

/**
 * 电站详情中的报表(HPS、PCS)
 */
class PlantTabChartFragment :
    BaseFragment(),
    View.OnClickListener {

    private var _binding: FragmentPlantTabChartBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PlantInfoViewModel by activityViewModels()

    private val chartTypes = PlantInfoResultModel.createChartType()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantTabChartBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getChartLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                showData(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun showData(chartListDataModel: ChartListDataModel?) {
        binding.root.visible()
        chartListDataModel?.also {
            val chartFragment = childFragmentManager.findFragmentById(R.id.fragment_chart)
            val typeName = binding.tabLayout.getTabText(binding.tabLayout.getSelectTabPosition())
            if (isShowLineChart()) {
                val unit =
                    if (viewModel.dataType.type == chartTypes[0].type) MainApplication.instance()
                        .getString(R.string.kw) else viewModel.dataType.typeUnit
                if (chartFragment is LineChartFragment) {
                    chartFragment.refresh(chartListDataModel, unit, typeName)
                } else {
                    childFragmentManager.commit(true) {
                        replace(
                            R.id.fragment_chart,
                            LineChartFragment(chartListDataModel, unit, typeName)
                        )
                    }
                }
            } else {
                if (chartFragment is BarChartFragment) {
                    chartFragment.refresh(chartListDataModel, viewModel.dataType.typeUnit, typeName)
                } else {
                    childFragmentManager.commit(true) {
                        replace(
                            R.id.fragment_chart,
                            BarChartFragment(
                                chartListDataModel,
                                viewModel.dataType.typeUnit,
                                typeName
                            )
                        )
                    }
                }
            }
        }
    }

    private fun isShowLineChart(): Boolean {
        if (viewModel.dataType.type == chartTypes[0].type && viewModel.dateType == DateType.HOUR) {
            return true
        }
        if (viewModel.dataType.type == chartTypes[1].type || viewModel.dataType.type == chartTypes[2].type) {
            return true
        }
        return false
    }

    private fun setListener() {
        binding.tabLayout.addTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelect(selectTab: Tab, selectPosition: Int) {
                when (selectPosition) {
                    0 -> {
                        viewModel.dataType = chartTypes[0]
                        binding.chartTimeSelectLayout.visible()
                    }
                    1 -> {
                        viewModel.dataType = chartTypes[1]
                        binding.chartTimeSelectLayout.invisible()
                    }
                    2 -> {
                        viewModel.dataType = chartTypes[2]
                        binding.chartTimeSelectLayout.invisible()
                    }
                }
                showDialog()
                viewModel.getChartInfo()
            }
        })
        binding.chartTimeSelectLayout.setChartTimeSelectListener { dateType, date ->
            viewModel.dateType = dateType
            viewModel.selectDate = date
            showDialog()
            viewModel.getChartInfo()
        }
    }

    private fun initView() {
        binding.tabLayout.setSelectTabPosition(0, false)
        binding.root.gone()
    }

    override fun onClick(v: View?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}