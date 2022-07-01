package com.growatt.atess.ui.mine.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.growatt.atess.R
import com.growatt.atess.databinding.SettingItemViewBinding
import com.growatt.lib.util.gone
import com.growatt.lib.util.invisible
import com.growatt.lib.util.visible

/**
 * 自定义组合View-设置页面Item
 */
class SettingItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: SettingItemViewBinding

    private var leftIcon: Drawable?
    private var showLeftIcon: Boolean = false
    private var showRightIcon: Boolean = true
    private var showSubName: Boolean = false
    private var showRedPoint: Boolean = false
    private var textSubNameColor: Int = resources.getColor(R.color.text_gray_66)
    private var itemName: String?
    private var subName: String?

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.setting_item_view, this)
        binding = SettingItemViewBinding.bind(view)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SettingItemView,
            0, 0
        ).apply {
            try {
                leftIcon = getDrawable(R.styleable.SettingItemView_leftIcon)
                showLeftIcon = getBoolean(R.styleable.SettingItemView_showLeftIcon, false)
                showRightIcon = getBoolean(R.styleable.SettingItemView_showRightIcon, true)
                showSubName = getBoolean(R.styleable.SettingItemView_showSubName, false)
                showRedPoint = getBoolean(R.styleable.SettingItemView_showRedPoint, false)
                textSubNameColor =
                    getColor(R.styleable.SettingItemView_textSubNameColor, textSubNameColor)
                itemName = getString(R.styleable.SettingItemView_itemName) ?: ""
                subName = getString(R.styleable.SettingItemView_subName) ?: ""
            } finally {
                recycle()
            }
        }
        initView()
    }

    private fun initView() {
        if (showLeftIcon) {
            binding.ivLeft.setImageDrawable(leftIcon)
            binding.ivLeft.visible()
        } else {
            binding.ivLeft.gone()
        }
        if (showRightIcon) {
            binding.ivRight.visible()
        } else {
            binding.ivRight.invisible()
        }
        if (showSubName) {
            binding.tvItemSubName.visible()
        } else {
            binding.tvItemSubName.gone()
        }
        if (showRedPoint) {
            binding.tvRedPoint.visible()
        } else {
            binding.tvRedPoint.gone()
        }
        binding.tvItemSubName.setTextColor(textSubNameColor)
        binding.tvItemName.text = itemName
        setSubName(subName)
    }

    fun setSubName(subName: String?) {
        if (showSubName) {
            this.subName = subName
            binding.tvItemSubName.text = subName ?: ""
        }
    }

    fun getSubName(): String {
        return binding.tvItemSubName.text.toString()
    }
}