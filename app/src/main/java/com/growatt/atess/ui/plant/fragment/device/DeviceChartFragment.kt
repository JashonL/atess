package com.growatt.atess.ui.plant.fragment.device

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentDeviceChartBinding
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.model.plant.ChartTypeModel
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.HpsModel
import com.growatt.atess.ui.plant.viewmodel.DeviceInfoViewModel
import com.growatt.lib.util.*
import java.util.*

/**
 * 设备详情中的报表(HPS、PCS、PBD、MBMS、BMS)
 * @param deviceType 设备类型
 * @param types 图表类型
 */
class DeviceChartFragment(@DeviceType val deviceType: Int, val types: Array<ChartTypeModel>) :
    BaseFragment(),
    View.OnClickListener {

    companion object {
        const val MINUTES_INTERVAL = 60 * 1000
    }

    private lateinit var binding: FragmentDeviceChartBinding

    private val viewModel: DeviceInfoViewModel<HpsModel> by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceChartBinding.inflate(inflater, container, false)
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
        binding.tvDataType.text = viewModel.chartType?.typeName
        binding.tvDate.text = DateUtils.yyyy_MM_dd_format(viewModel.selectDate)
        if (types.size > 1) {
            binding.tvDataType.setDrawableEnd(resources.getDrawable(R.drawable.ic_down))
        } else {
            binding.tvDataType.setDrawableNull()
        }
        initChartView()
    }

    private fun setDeviceChartData(chartListDataModel: ChartListDataModel?) {
        if (chartListDataModel == null) {
            return
        }

        val timeList = chartListDataModel.getXTimeList()
        val chartYDataList = chartListDataModel.getYDataList()
        val lineData = LineData().also {
            it.isHighlightEnabled = true
        }
        for (i in chartYDataList.indices) {
            //设置一条折线数据
            val lineDataValues = mutableListOf<Entry>()
            val chartYData = chartYDataList[i]//一条折线数据
            for (yDataIndex in chartYData.getYDataList().indices) {
                val time =
                    DateUtils.HH_mm_format.parse(timeList[yDataIndex]).time / MINUTES_INTERVAL
                val y = chartYData.getYDataList()[yDataIndex]
                lineDataValues.add(Entry(time.toFloat(), y))
            }

            //X轴
            binding.mpChart.xAxis.also {
                it.axisMaximum = lineDataValues[lineDataValues.size - 1].x//设置坐标的最大值
                it.axisMinimum = lineDataValues[0].x//设置坐标的最小值
            }

            val lineDataSet = LineDataSet(lineDataValues, chartYData.type).also {
                it.mode = LineDataSet.Mode.CUBIC_BEZIER
                it.setDrawCircles(false)//画原点
                it.setCircleColor(Color.parseColor("#FFFEB64D"))
                it.setDrawFilled(true)
                it.fillColor = Color.parseColor("#33FEB64D")//设置fill区域的颜色
                it.fillAlpha = 100//设置fill区域的颜色的透明度
                it.highLightColor = resources.getColor(R.color.colorAccent)
                it.setDrawVerticalHighlightIndicator(true)
                it.setDrawHorizontalHighlightIndicator(false)
                it.enableDashedHighlightLine(4f, 4f, 0f)
                it.highlightLineWidth = 0.8f
                it.setDrawValues(false)//是否显示点的值
                it.color = Color.parseColor("#FFFEB64D")//设置曲线的颜色
            }
            lineData.addDataSet(lineDataSet)
        }
        binding.mpChart.data = lineData
        binding.mpChart.invalidate()
        binding.mpChart.animateXY(1000, 1000)
    }

    private fun initChartView() {
        binding.mpChart.also {
            it.setDrawGridBackground(false)
            it.description.isEnabled = false //不显示描述（右下角）
            it.setTouchEnabled(true)//设置是否能触摸
            it.isScaleXEnabled = true//设置X轴能缩小放大,配合setTouchEnabled使用
            it.isScaleYEnabled = false//设置Y轴能缩小放大,配合setTouchEnabled使用
            it.isDragEnabled = true//设置能够拖动
            it.axisRight.isEnabled = false//设置右边Y轴线不显示
            it.legend.isEnabled = false//是否显示类型图标
        }

        //X轴
        binding.mpChart.xAxis.also {
            it.isEnabled = true //设置X轴启用
            it.position = XAxis.XAxisPosition.BOTTOM //设置X轴坐标值显示的位置
            it.setDrawGridLines(false) //设置y轴坐标值是否需要画竖线
            it.setDrawLabels(true)
            it.textColor = resources.getColor(R.color.text_gray_99) //设置x轴文本颜色
            it.setDrawAxisLine(true) //设置是否绘制轴线
            it.axisLineColor = resources.getColor(R.color.color_line)//设置x轴线的颜色
            it.axisLineWidth = 2f//设置x轴线宽度
            it.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return DateUtils.HH_mm_format(Date((value * 60 * 1000).toLong()))
                }
            }
        }

        //Y轴
        binding.mpChart.axisLeft.also {
            it.axisLineColor = resources.getColor(android.R.color.transparent)//设置Y轴线的颜色
            it.textColor = resources.getColor(R.color.text_gray_99) //设置Y轴文本颜色
            it.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Util.getDoubleText(value.toDouble())
                }
            }
            it.axisMinimum = 0f//设置坐标的最小值
            it.setDrawLabels(true)
            it.setDrawAxisLine(false)
            it.setDrawGridLines(true) //设置x轴坐标值是否需要画横线
            it.enableGridDashedLine(4f, 4f, 0f)
            it.gridLineWidth = 0.8f
            it.gridColor = Color.parseColor("#F2F2F2")

            it.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)//设置Y轴坐标值显示的位置
        }
        binding.mpChart.invalidate()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvDataType -> {}
            v === binding.ivAdd -> {
                val nextDay = DateUtils.addDateDays(Date(viewModel.selectDate), 1)
                if (nextDay <= Date()) {
                    viewModel.selectDate = nextDay.time
                    binding.tvDate.text = DateUtils.yyyy_MM_dd_format(viewModel.selectDate)
                    showDialog()
                    viewModel.getDeviceChartInfo(deviceType)
                }

            }
            v === binding.ivReduce -> {
                viewModel.selectDate = DateUtils.addDateDays(Date(viewModel.selectDate), -1).time
                binding.tvDate.text = DateUtils.yyyy_MM_dd_format(viewModel.selectDate)
                showDialog()
                viewModel.getDeviceChartInfo(deviceType)
            }
            v === binding.tvDate -> {

            }
        }
    }
}