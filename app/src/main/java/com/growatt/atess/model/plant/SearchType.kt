package com.growatt.atess.model.plant

import androidx.annotation.IntDef

/**
 * 搜索Type
 */
@IntDef(
    SearchType.PLANT,
    SearchType.DEVICE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class SearchType {
    companion object {
        const val PLANT = 1
        const val DEVICE = 2
    }
}