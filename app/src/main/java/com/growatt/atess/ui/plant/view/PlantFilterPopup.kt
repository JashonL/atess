package com.growatt.atess.ui.plant.view

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.growatt.atess.R
import com.growatt.atess.model.plant.PlantFilterModel
import com.growatt.lib.util.ViewUtil

/**
 * 电站筛选过滤器
 */
class PlantFilterPopup(
    private val context: Context,
    private val widthInt: Int,
    private val heightInt: Int,
    private val selectedFilterModel: PlantFilterModel,
    private val callback: (selectFilterModel: PlantFilterModel) -> Unit
) : PopupWindow() {

    companion object {

        fun show(
            context: Context,
            anchorView: View,
            widthInt: Int,
            heightInt: Int,
            selectedFilterModel: PlantFilterModel,
            callback: (selectFilterModel: PlantFilterModel) -> Unit
        ) {
            val popup =
                PlantFilterPopup(context, widthInt, heightInt, selectedFilterModel, callback)
            popup.init()
            popup.showAsDropDown(anchorView)
        }
    }

    private val filterModels = PlantFilterModel.createFilters()

    private fun init() {
        width = LinearLayout.LayoutParams.MATCH_PARENT
        height = heightInt
        contentView = generateContentView()
        isOutsideTouchable = true
    }

    private fun generateContentView(): View {
        val frameLayout = FrameLayout(context).also {
            it.setBackgroundResource(R.color.color_33000000)
            it.setPadding(ViewUtil.dp2px(context, 16f), 0, 0, 0)
        }

        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                widthInt,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundResource(R.drawable.white_background_corner_2_stroke_1)
            for (filter in filterModels) {
                addView(TextView(context).also {
                    it.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    it.setPadding(
                        ViewUtil.dp2px(context, 10f),
                        ViewUtil.dp2px(context, 7f),
                        0,
                        ViewUtil.dp2px(context, 7f)
                    )
                    it.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        context.resources.getDimension(R.dimen.text_small)
                    )
                    if (selectedFilterModel == filter) {
                        it.setTextColor(context.resources.getColor(R.color.text_red))
                    } else {
                        it.setTextColor(context.resources.getColor(R.color.text_gray_99))
                    }

                    it.text = filter.filterName
                    it.setOnClickListener {
                        if (selectedFilterModel != filter) {
                            callback.invoke(filter)
                        }
                        dismiss()
                    }
                })
            }
        }

        frameLayout.addView(linearLayout)
        frameLayout.setOnClickListener {
            dismiss()
        }
        return frameLayout
    }

}