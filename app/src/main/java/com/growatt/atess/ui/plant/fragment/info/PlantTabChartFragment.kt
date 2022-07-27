package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantTabChartBinding
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.ui.plant.fragment.LineChartFragment
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.atess.view.DateType
import com.growatt.atess.view.OnTabSelectedListener
import com.growatt.atess.view.Tab
import com.growatt.lib.util.ToastUtil

/**
 * 电站详情中的报表(HPS、PCS)
 */
class PlantTabChartFragment :
    BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentPlantTabChartBinding

    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantTabChartBinding.inflate(inflater, container, false)
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
        chartListDataModel?.also {
            if (isShowLineChart()) {
                childFragmentManager.commit(true) {
                    replace(
                        R.id.fragment_chart,
                        LineChartFragment(chartListDataModel, getString(R.string.kwh))
                    )
                }
            }
        }
    }

    private fun isShowLineChart(): Boolean {
        if (viewModel.dataType == "1" && viewModel.dateType == DateType.HOUR) {
            return true
        }
        if (viewModel.dataType == "2" || viewModel.dataType == "3") {
            return true
        }
        return false
    }

    private fun setListener() {
        binding.tabLayout.addTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelect(selectTab: Tab, selectPosition: Int) {
                when (selectPosition) {
                    0 -> {
                        viewModel.dataType = "1"
                    }
                    1 -> {
                        viewModel.dataType = "2"
                    }
                    2 -> {
                        viewModel.dataType = "3"
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
    }

    override fun onClick(v: View?) {

    }
}