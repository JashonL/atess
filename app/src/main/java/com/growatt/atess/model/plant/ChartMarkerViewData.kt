package com.growatt.atess.model.plant

import androidx.annotation.ColorInt

/**
 * 图表MarkerView展示Model
 */
data class ChartMarkerViewData(val timeText: String, val values: Array<ChartDataValue>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChartMarkerViewData

        if (timeText != other.timeText) return false
        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timeText.hashCode()
        result = 31 * result + values.contentHashCode()
        return result
    }
}

/**
 * 图表数值Model
 */
data class ChartDataValue(@ColorInt val color: Int, val label: String, val valueText: String)