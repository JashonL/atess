package com.growatt.atess.ui.plant.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentLineChartBinding
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.lib.util.DateUtils
import com.growatt.lib.util.Util
import java.util.*

/**
 * 折线图表
 */
class LineChartFragment(var chartListDataModel: ChartListDataModel? = null, val unit: String) :
    BaseFragment() {

    companion object {
        const val MINUTES_INTERVAL = 60 * 1000
    }

    private lateinit var binding: FragmentLineChartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLineChartBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }


    private fun initView() {
        binding.tvUnit.text = unit
        initChartView()
    }

    fun refresh(chartListDataModel: ChartListDataModel?) {
        this.chartListDataModel = chartListDataModel
        showChartData()
    }

    private fun showChartData() {
        if (chartListDataModel == null) {
            return
        }

        val timeList = chartListDataModel!!.getXTimeList()
        val chartYDataList = chartListDataModel!!.getYDataList()
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
            binding.lineChart.xAxis.also {
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
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()
        binding.lineChart.animateXY(1000, 1000)
    }

    private fun initChartView() {
        binding.lineChart.also {
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
        binding.lineChart.xAxis.also {
            it.isEnabled = true //设置X轴启用
            it.position = XAxis.XAxisPosition.BOTTOM //设置X轴坐标值显示的位置
            it.setDrawGridLines(false) //设置y轴坐标值是否需要画竖线
            it.setDrawLabels(true)//显示X轴坐标值
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
        binding.lineChart.axisLeft.also {
            it.axisLineColor = resources.getColor(android.R.color.transparent)//设置Y轴线的颜色
            it.textColor = resources.getColor(R.color.text_gray_99) //设置Y轴文本颜色
            it.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Util.getDoubleText(value.toDouble())
                }
            }
            it.axisMinimum = 0f//设置坐标的最小值
            it.setDrawLabels(true)//显示Y轴坐标值
            it.setDrawAxisLine(false)//设置是否绘制轴线
            it.setDrawGridLines(true) //设置x轴坐标值是否需要画横线
            it.enableGridDashedLine(4f, 4f, 0f)
            it.gridLineWidth = 0.8f
            it.gridColor = Color.parseColor("#F2F2F2")
            it.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)//设置Y轴坐标值显示的位置
        }
        binding.lineChart.invalidate()
    }
}