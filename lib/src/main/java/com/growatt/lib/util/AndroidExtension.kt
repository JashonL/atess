package com.growatt.lib.util

import android.view.View
import android.widget.TextView


fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun TextView.textLength(): Int {
    return length()
}

fun View.setViewHeight(dpFloat: Float) {
    val newLayoutParams = layoutParams
    newLayoutParams.height = ViewUtil.dp2px(context, dpFloat)
    layoutParams = newLayoutParams
}