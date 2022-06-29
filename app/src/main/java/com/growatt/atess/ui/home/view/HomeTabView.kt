package com.growatt.atess.ui.home.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.growatt.atess.R
import com.growatt.atess.databinding.HomeTabViewBinding

class HomeTabView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: HomeTabViewBinding

    private var normalIcon: Drawable?
    private var selectIcon: Drawable?
    private var textNormalColor: Int = resources.getColor(R.color.text_gray_99)
    private var textSelectColor: Int = resources.getColor(R.color.text_black)
    private var isSelect: Boolean = false
    private var text: String = ""

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.home_tab_view, this)
        binding = HomeTabViewBinding.bind(view)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HomeTabView,
            0, 0
        ).apply {
            try {
                normalIcon = getDrawable(R.styleable.HomeTabView_normalIcon)
                selectIcon = getDrawable(R.styleable.HomeTabView_selectIcon)
                textNormalColor = getColor(R.styleable.HomeTabView_textNormalColor, textNormalColor)
                textSelectColor = getColor(R.styleable.HomeTabView_textSelectColor, textSelectColor)
                isSelect = getBoolean(R.styleable.HomeTabView_isSelected, false)
                text = getString(R.styleable.HomeTabView_text).toString()
            } finally {
                recycle()
            }
        }
        binding.tvName.text = text
        updateView()
    }

    private fun updateView() {
        if (isSelect) {
            binding.ivIcon.setImageDrawable(selectIcon)
            binding.tvName.setTextColor(textSelectColor)
        } else {
            binding.ivIcon.setImageDrawable(normalIcon)
            binding.tvName.setTextColor(textNormalColor)
        }
    }

    fun setSelect(isSelect: Boolean) {
        if (this.isSelect == isSelect) {
            return
        }
        this.isSelect = isSelect
        updateView()
    }

}