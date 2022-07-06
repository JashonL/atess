package com.growatt.atess.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.growatt.atess.R
import com.growatt.atess.databinding.TabItemBinding

/**
 * 自定义组合View-Tab
 */
class Tab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: TabItemBinding

    private var tabText: String? = null

    private var isSelect = false

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.tab_item, this)
        binding = TabItemBinding.bind(view)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Tab,
            0, 0
        ).apply {
            try {
                tabText = getString(R.styleable.Tab_tabText) ?: ""
            } finally {
                recycle()
            }
        }
        initView()
    }

    private fun initView() {
        binding.tvTab.text = tabText
        updateView(isSelect)
    }

    fun setTabText(tabText: String?) {
        this.tabText = tabText
        binding.tvTab.text = tabText ?: ""
    }

    fun setSelect(isSelect: Boolean) {
        if (this.isSelect == isSelect) {
            return
        }
        updateView(isSelect)
    }

    fun isSelect(): Boolean {
        return isSelect
    }

    private fun updateView(isSelect: Boolean) {
        this.isSelect = isSelect
        binding.tvTab.apply {
            typeface = if (isSelect) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(if (isSelect) R.dimen.text_subtitle else R.dimen.text_medium)
            )
            setTextColor(context.resources.getColor(if (isSelect) R.color.text_black else R.color.text_gray_99))
        }
        binding.indicator.visibility = if (isSelect) VISIBLE else INVISIBLE
    }
}