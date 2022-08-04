package com.growatt.atess.model.plant

import androidx.annotation.IntDef

/**
 * 设备流向
 */
@IntDef(
    DirectionType.OUTPUT,
    DirectionType.INPUT,
    DirectionType.HIDE,
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class DirectionType {
    companion object {
        /**
         * 流出
         */
        const val OUTPUT = -1

        /**
         * 流入
         */
        const val INPUT = 2

        /**
         * 无数据隐藏
         */
        const val HIDE = 0
    }
}