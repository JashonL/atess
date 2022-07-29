package com.growatt.atess.ui.plant.fragment.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentDeviceChartBinding
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.model.plant.ChartTypeModel
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.ui.plant.fragment.LineChartFragment
import com.growatt.atess.ui.plant.viewmodel.BaseDeviceInfoViewModel
import com.growatt.atess.view.dialog.OptionsDialog
import com.growatt.lib.util.*
import com.growatt.lib.view.dialog.DatePickerFragment
import com.growatt.lib.view.dialog.OnDateSetListener
import java.util.*

/**
 * 设备详情中的报表(HPS、PCS、PBD、MBMS、BMS)
 * @param deviceType 设备类型
 * @param types 图表类型
 */
class DeviceChartFragment(
    @DeviceType val deviceType: Int,
    private val types: Array<ChartTypeModel>,
    private val viewModel: BaseDeviceInfoViewModel
) :
    BaseFragment(),
    View.OnClickListener {
    private var _binding: FragmentDeviceChartBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceChartBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        if (viewModel.chartType == null) {
            viewModel.chartType = types[0]
        }
        viewModel.getDeviceChartLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                setDeviceChartData(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getDeviceChartInfo(deviceType)
    }

    private fun setListener() {
        binding.tvDataType.setOnClickListener(this)
        binding.ivAdd.setOnClickListener(this)
        binding.ivReduce.setOnClickListener(this)
        binding.tvDate.setOnClickListener(this)
    }

    private fun initView() {
        binding.llSelectDate.background =
            ViewUtil.createShape(resources.getColor(R.color.color_0D000000), 30)
        refreshDataTypeView()
        refreshDateView()
        if (types.size > 1) {
            binding.tvDataType.setDrawableEnd(resources.getDrawable(R.drawable.ic_down))
        } else {
            binding.tvDataType.setDrawableNull()
        }
    }

    private fun setDeviceChartData(chartListDataModel: ChartListDataModel?) {
        if (chartListDataModel == null) {
            return
        }
        val chartFragment = childFragmentManager.findFragmentById(R.id.fragment_line_chart)
        if (chartFragment is LineChartFragment) {
            chartFragment.refresh(chartListDataModel, viewModel.chartType?.typeUnit ?: "")
        } else {
            childFragmentManager.commit(true) {
                add(
                    R.id.fragment_line_chart,
                    LineChartFragment(chartListDataModel, viewModel.chartType?.typeUnit ?: "")
                )
            }
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvDataType -> showSelectChartType()
            v === binding.ivAdd -> {
                val nextDay = DateUtils.addDateDays(Date(viewModel.selectDate), 1)
                if (nextDay <= Date()) {
                    viewModel.selectDate = nextDay.time
                    refreshDateView()
                    showDialog()
                    viewModel.getDeviceChartInfo(deviceType)
                }
            }
            v === binding.ivReduce -> {
                viewModel.selectDate = DateUtils.addDateDays(Date(viewModel.selectDate), -1).time
                refreshDateView()
                refreshDataTypeView()
                showDialog()
                viewModel.getDeviceChartInfo(deviceType)
            }
            v === binding.tvDate -> showDatePicker()
        }
    }

    private fun refreshDataTypeView() {
        binding.tvDataType.text = viewModel.chartType?.typeName
    }

    private fun showSelectChartType() {
        if (types.size > 1) {
            OptionsDialog.show(childFragmentManager, ChartTypeModel.getTypeNames(types)) {
                val selectChartType = types[it]
                if (viewModel.chartType?.type != selectChartType.type) {
                    viewModel.chartType = selectChartType
                    refreshDataTypeView()
                    showDialog()
                    viewModel.getDeviceChartInfo(deviceType)
                }
            }
        }
    }

    private fun refreshDateView() {
        binding.tvDate.text = DateUtils.yyyy_MM_dd_format(viewModel.selectDate)
    }

    private fun showDatePicker() {
        DatePickerFragment.show(childFragmentManager, System.currentTimeMillis(), object :
            OnDateSetListener {
            override fun onDateSet(date: Date) {
                viewModel.selectDate = date.time
                refreshDateView()
                showDialog()
                viewModel.getDeviceChartInfo(deviceType)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}