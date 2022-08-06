package com.growatt.atess.ui.plant.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.growatt.atess.R
import com.growatt.atess.databinding.ChartLabelValueBinding
import com.growatt.atess.model.plant.ChartDataValue
import com.growatt.atess.model.plant.ChartMarkerViewData
import com.growatt.lib.util.ViewUtil

@SuppressLint("ViewConstructor")
class ChartMarkerView(context: Context?, layoutResource: Int) :
    MarkerView(context, layoutResource) {

    var data: ChartMarkerViewData? = null

    private val tvTime: TextView = findViewById(R.id.tv_time)
    private val llLabels: LinearLayout = findViewById(R.id.ll_labels)

    override fun refreshContent(e: Entry, highlight: Highlight) {
        tvTime.text = data?.timeText
        llLabels.removeAllViews()
        data?.values?.also {
            for (value in it) {
                llLabels.addView(generateItemView(value))
            }
        }

        super.refreshContent(e, highlight)
    }

    private fun generateItemView(data: ChartDataValue): View {
        val binding = ChartLabelValueBinding.inflate(LayoutInflater.from(context), llLabels, false)
        binding.ivColor.setImageDrawable(ViewUtil.createShape(data.color, 3))
        binding.tvLabel.text = data.label
        binding.tvValue.text = data.valueText
        binding.root.background = ViewUtil.createShape(Color.WHITE, 3)
        (binding.root.layoutParams as MarginLayoutParams).bottomMargin = ViewUtil.dp2px(context, 4f)
        return binding.root
    }

    override fun getOffset(): MPPointF {
        return MPPointF(ViewUtil.dp2px(context, 4f).toFloat(), (-height).toFloat())
    }

}