package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentLineChartBinding
import com.growatt.atess.model.plant.ChartDataValue
import com.growatt.atess.model.plant.ChartListDataModel
import com.growatt.atess.model.plant.ChartMarkerViewData
import com.growatt.atess.model.plant.ChartTypeModel
import com.growatt.atess.ui.plant.view.ChartMarkerView
import com.growatt.lib.util.DateUtils
import com.growatt.lib.util.Util
import java.util.*

/**
 * 折线图表
 * @param typeName-大类别（只有一种图标数据使用这个展示）
 */
class LineChartFragment(
    private var chartListDataModel: ChartListDataModel? = null,
    var unit: String,
    var typeName: String? = null
) :
    BaseFragment(), IChartRefreshListener {

    companion object {
        const val MINUTES_INTERVAL = 60 * 1000
    }

    private var _binding: FragmentLineChartBinding? = null

    private val binding get() = _binding!!

    private val colors = ChartTypeModel.createChartColors()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLineChartBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }


    private fun initView() {
        binding.tvUnit.text = unit
        initChartView()
        showChartData()
    }

    override fun refresh(
        chartListDataModel: ChartListDataModel?,
        unit: String,
        typeName: String?
    ) {
        this.chartListDataModel = chartListDataModel
        this.typeName = typeName
        this.unit = unit
        binding.tvUnit.text = unit
        showChartData()
    }

    private fun showChartData() {
        if (chartListDataModel == null) {
            return
        }

        //设置highlight为空，刷新后不显示MarkerView
        binding.lineChart.highlightValue(null)

        val timeList = chartListDataModel!!.getXTimeList()
        val chartYDataList = chartListDataModel!!.getYDataList()
        val lineData = LineData().also {
            it.isHighlightEnabled = true
        }
        var granularity: Float? = null//X轴数据间隔
        for (i in chartYDataList.indices) {
            //设置一条折线数据
            val lineDataValues = mutableListOf<Entry>()
            val chartYData = chartYDataList[i]//一条折线数据
            if (chartYData.getYDataList().isNullOrEmpty()) {
                continue
            }
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
            val lineDataSet = LineDataSet(
                lineDataValues,
                if (chartYDataList.size == 1) typeName else chartYData.getTypeName()
            ).also {
                val color = colors[i % colors.size]
                it.mode = LineDataSet.Mode.CUBIC_BEZIER
                it.setDrawCircles(false)//画原点
                it.setCircleColor(color.color)
                it.setDrawFilled(true)
                it.fillColor = color.alphaColor//设置fill区域的颜色
                it.fillAlpha = 100//设置fill区域的颜色的透明度
                it.highLightColor = resources.getColor(R.color.colorAccent)
                it.setDrawVerticalHighlightIndicator(true)
                it.setDrawHorizontalHighlightIndicator(false)
                it.enableDashedHighlightLine(4f, 4f, 0f)
                it.highlightLineWidth = 0.8f
                it.setDrawValues(false)//是否显示点的值
                it.color = color.color//设置曲线的颜色
            }
            if (granularity == null && lineDataValues.size > 2) {
                granularity = lineDataValues[1].x - lineDataValues[0].x
            }
            lineData.addDataSet(lineDataSet)
        }

        binding.lineChart.xAxis.granularity = granularity ?: 5f//根据X轴的数据，设置坐标的间隔尺度

        //设置图例，数据种类颜色标识
        if (chartListDataModel?.dataList?.size ?: 0 > 1) {
            binding.lineChart.legend.also {
                it.isEnabled = true//是否显示类型图例
                it.form = LegendForm.LINE//图标样式
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
            binding.lineChart.legend.isEnabled = false //是否显示类型图例
        }

        binding.lineChart.data = lineData
        binding.lineChart.invalidate()
        binding.lineChart.animateXY(1000, 1000)
    }

    private fun initChartView() {
        val marker = ChartMarkerView(requireContext(), R.layout.chart_marker_view)
        binding.lineChart.also {
            it.setDrawGridBackground(false)
            it.description.isEnabled = false //不显示描述（右下角）
            it.setTouchEnabled(true)//设置是否能触摸
            it.isScaleXEnabled = true//设置X轴能缩小放大,配合setTouchEnabled使用
            it.isScaleYEnabled = false//设置Y轴能缩小放大,配合setTouchEnabled使用
            it.isDragEnabled = true//设置能够拖动
            it.axisRight.isEnabled = false//设置右边Y轴线不显示
            it.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    val dataSets = it.data.dataSets
                    val chartDataValues: MutableList<ChartDataValue> = mutableListOf()
                    val x = e?.x ?: 0f
                    for (index in dataSets.indices) {
                        val lineDataSet = dataSets[index]
                        val y = lineDataSet.getEntryForXValue(e?.x ?: 0f, lineDataSet.yMax).y
                        chartDataValues.add(
                            ChartDataValue(
                                lineDataSet.color,
                                lineDataSet.label,
                                Util.getDoubleText(y.toDouble())
                            )
                        )
                        if (index == 0) {
                            it.highlightValue(Highlight(x, y, index))
                        }
                    }
                    marker.data = ChartMarkerViewData(
                        DateUtils.HH_mm_format(Date(x.toLong() * MINUTES_INTERVAL)),
                        chartDataValues.toTypedArray()
                    )
                }

                override fun onNothingSelected() {

                }

            })
            marker.chartView = binding.lineChart
            binding.lineChart.marker = marker
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
                    return DateUtils.HH_mm_format(Date((value * MINUTES_INTERVAL).toLong()))
                }
            }
        }

        //Y轴
        binding.lineChart.axisLeft.also {
            it.axisLineColor = resources.getColor(android.R.color.transparent)//设置Y轴线的颜色
            it.textColor = resources.getColor(R.color.text_gray_99) //设置Y轴文本颜色
            it.axisMinimum = 0f//设置坐标的最小值
            it.setDrawLabels(true)//显示Y轴坐标值
            it.setDrawAxisLine(false)//设置是否绘制轴线
            it.setDrawGridLines(true) //设置x轴坐标值是否需要画横线
            it.enableGridDashedLine(4f, 4f, 0f)
            it.gridLineWidth = 0.8f
//            it.gridColor = Color.parseColor("#F2F2F2") //设置Y轴线颜色
            it.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)//设置Y轴坐标值显示的位置
            it.spaceTop = 35f//设置Y轴最大值比数据的最大值大，不设置的话，数据的最大值就是Y轴的最大值
            it.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Util.getDoubleText(value.toDouble())
                }
            }
        }
        binding.lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface IChartRefreshListener {
    fun refresh(chartListDataModel: ChartListDataModel?, unit: String, typeName: String? = null)
}