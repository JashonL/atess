package com.growatt.atess.ui.plant.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentBarChartBinding
import com.growatt.atess.model.plant.ChartDataValue
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.model.plant.ChartMarkerViewData
import com.growatt.atess.model.plant.ChartTypeModel
import com.growatt.atess.ui.plant.view.ChartMarkerView
import com.growatt.lib.util.Util
import kotlin.math.floor

/**
 * 柱状图表
 */
class BarChartFragment(var chartListDataModel: ChartListDataModel? = null, var unit: String) :
    BaseFragment(), IChartRefreshListener {

    private var _binding: FragmentBarChartBinding? = null

    private val binding get() = _binding!!

    private val colors = ChartTypeModel.createChartColors()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarChartBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }


    private fun initView() {
        binding.tvUnit.text = unit
        initChartView()
        showChartData()
    }

    override fun refresh(chartListDataModel: ChartListDataModel?, unit: String) {
        this.chartListDataModel = chartListDataModel
        showChartData()
        this.unit = unit
        binding.tvUnit.text = unit
    }

    private fun showChartData() {
        if (chartListDataModel == null) {
            return
        }
        //设置highlight为空，刷新后不显示MarkerView
        binding.barChart.highlightValue(null)

        val timeList = chartListDataModel!!.getXTimeList()
        val chartYDataList = chartListDataModel!!.getYDataList()
        val barData = BarData().also {
            it.isHighlightEnabled = true
        }//数据源
        var granularity: Float? = null//X轴数据间隔
        for (i in chartYDataList.indices) {
            //设置一种柱状图数据
            val barDataValues = mutableListOf<BarEntry>()
            val chartYData = chartYDataList[i]//一种柱状图数据
            if (chartYData.getYDataList().isNullOrEmpty()) {
                continue
            }
            for (yDataIndex in chartYData.getYDataList().indices) {
                val time = timeList[yDataIndex]
                val y = chartYData.getYDataList()[yDataIndex]
                barDataValues.add(BarEntry(time.toFloat(), y))
            }

            val color = colors[i % colors.size]
            val barDataSet = BarDataSet(barDataValues, chartYData.getTypeName()).also {
                it.setDrawValues(false)//是否显示点的值
                it.color = color.color//设置曲线的颜色
                it.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return Util.getDoubleText(value.toDouble())
                    }
                }
            }
            if (granularity == null && barDataValues.size > 2) {
                granularity = barDataValues[1].x - barDataValues[0].x
            }
            barData.addDataSet(barDataSet)
        }

        binding.barChart.xAxis.granularity = granularity ?: 1f//根据X轴的数据，设置坐标的间隔尺度

        //设置图例，数据种类颜色标识
        if (chartListDataModel?.dataList?.size ?: 0 > 1) {
            binding.barChart.legend.also {
                it.isEnabled = true//是否显示类型图标
                it.form = Legend.LegendForm.LINE//图例样式
                it.formLineWidth = 2f//线条宽度
                it.textSize = 11f
                it.textColor = resources.getColor(R.color.text_black)
                it.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM//位置位于底部
                it.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT//位置居左对齐
                it.orientation = Legend.LegendOrientation.HORIZONTAL//水平分布
                it.isWordWrapEnabled = true//开启自动换行
                it.xEntrySpace = 20f//设置左右间距
                it.yEntrySpace = 5f//设置上下间距
            }
        } else {
            binding.barChart.legend.isEnabled = false //是否显示类型图例
        }


        val groupWidth = 1f//设置一组柱状图宽度为1f,这里固定这样，无需修改
        //默认1条柱的值
        var groupSpace = 0.4f//组之间的间隔
        var barSpace = 0f //组内柱状图之间的间隔
        var barWidth = 0.6f // 柱状图宽度
        // 例如4条柱状图，(0.2 + 0) * 4 + 0.08 = 1.00 -> interval per "group",计算出一组柱状图的宽度大小
        val groupBarCount = barData.dataSetCount//一组柱状图的数量
        if (groupBarCount > 1) {
            barWidth = if (groupBarCount > 4) {
                0.1f
            } else {
                0.2f
            }
            groupSpace = groupWidth - (barWidth + barSpace) * groupBarCount
        }

        val startX = timeList[0].toFloat()
        val endX = startX + groupWidth * timeList.size
        //X轴
        binding.barChart.xAxis.also {
            //包头不包尾
            it.axisMinimum = startX//设置坐标的最小值
            it.axisMaximum = endX//设置坐标的最大值
        }
        barData.barWidth = barWidth
        binding.barChart.data = barData
        binding.barChart.groupBars(startX, groupSpace, barSpace)
        binding.barChart.invalidate()
        binding.barChart.animateXY(1000, 1000)
    }

    private fun initChartView() {
        val marker = ChartMarkerView(requireContext(), R.layout.chart_marker_view)
        binding.barChart.also {
            it.setDrawGridBackground(false)
            it.description.isEnabled = false //不显示描述（右下角）
            it.setTouchEnabled(true)//设置是否能触摸
            it.isScaleXEnabled = true//设置X轴能缩小放大,配合setTouchEnabled使用
            it.isScaleYEnabled = false//设置Y轴能缩小放大,配合setTouchEnabled使用
            it.isDragEnabled = true//设置能够拖动
            it.axisRight.isEnabled = false//设置右边Y轴线不显示
            it.legend.isEnabled = false//是否显示类型图标

            it.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    val dataSets = it.data.dataSets
                    val chartDataValues: MutableList<ChartDataValue> = mutableListOf()
                    val x = e?.x ?: 0f
                    for (index in dataSets.indices) {
                        val barDataSet = dataSets[index]
                        val y = barDataSet.getEntryForXValue(e?.x ?: 0f, barDataSet.yMax).y
                        chartDataValues.add(
                            ChartDataValue(
                                barDataSet.color,
                                barDataSet.label,
                                Util.getDoubleText(y.toDouble())
                            )
                        )
                        if (index == 0) {
                            it.highlightValue(Highlight(x, y, index))
                        }
                    }
                    marker.data = ChartMarkerViewData(
                        Util.getDoubleText(floor(x.toDouble())),
                        chartDataValues.toTypedArray()
                    )
                }

                override fun onNothingSelected() {

                }

            })

            marker.chartView = binding.barChart
            binding.barChart.marker = marker
        }

        //X轴
        binding.barChart.xAxis.also {
            it.isEnabled = true //设置X轴启用
            it.position = XAxis.XAxisPosition.BOTTOM //设置X轴坐标值显示的位置
            it.setDrawGridLines(true) //设置y轴坐标值是否需要画竖线
            it.setDrawLabels(true)//显示X轴坐标值
            it.setCenterAxisLabels(true)//设置坐标值居中显示，柱状图需要使用到
            it.textColor = resources.getColor(R.color.text_gray_99) //设置x轴文本颜色
            it.setDrawAxisLine(true) //设置是否绘制轴线
            it.axisLineColor = resources.getColor(R.color.color_line)//设置x轴线的颜色
            it.axisLineWidth = 2f//设置x轴线宽度
            it.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
        }

        //Y轴
        binding.barChart.axisLeft.also {
            it.axisLineColor = resources.getColor(android.R.color.transparent)//设置Y轴线的颜色
            it.textColor = resources.getColor(R.color.text_gray_99) //设置Y轴文本颜色
            it.axisMinimum = 0f//设置坐标的最小值
            it.setDrawLabels(true)//显示Y轴坐标值
            it.setDrawAxisLine(false)//设置是否绘制轴线
            it.setDrawGridLines(true) //设置x轴坐标值是否需要画横线
            it.enableGridDashedLine(4f, 4f, 0f)//设置破折线
            it.gridLineWidth = 0.8f
            it.gridColor = Color.parseColor("#F2F2F2")
            it.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)//设置Y轴坐标值显示的位置
            it.spaceTop = 35f//设置Y轴最大值比数据的最大值大，不设置的话，数据的最大值就是Y轴的最大值
            it.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Util.getDoubleText(value.toDouble())
                }
            }
        }
        binding.barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}