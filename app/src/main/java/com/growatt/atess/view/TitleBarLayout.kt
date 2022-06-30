package com.growatt.atess.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.growatt.atess.R
import com.growatt.atess.databinding.TitleBarLayoutBinding
import com.growatt.lib.util.invisible
import com.growatt.lib.util.visible

/**
 * 自定义组合View-标题栏
 */
class TitleBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val binding: TitleBarLayoutBinding

    private var showLeftBackIcon: Boolean = true
    private var showRightButton: Boolean = false
    private var titleText: String = ""
    private var leftIconClickListener: ((View?) -> Unit)? = null
    private var rightButtonClickListener: ((View?) -> Unit)? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.title_bar_layout, this)
        binding = TitleBarLayoutBinding.bind(view)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TitleBarLayout,
            0, 0
        ).apply {
            try {
                showLeftBackIcon =
                    getBoolean(R.styleable.TitleBarLayout_showLeftBackIcon, showLeftBackIcon)
                showRightButton =
                    getBoolean(R.styleable.TitleBarLayout_showRightButton, showRightButton)
                titleText = getString(R.styleable.TitleBarLayout_titleText).toString()
            } finally {
                recycle()
            }
        }
        initView()
    }

    private fun initView() {
        binding.tvTitle.text = titleText
        if (showRightButton) {
            binding.btRight.visible()
        } else {
            binding.btRight.invisible()
        }
        if (showLeftBackIcon) {
            binding.ivBack.visible()
        } else {
            binding.ivBack.invisible()
        }
        binding.ivBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivBack -> {
                if (leftIconClickListener == null) {
                    (context as? Activity)?.finish()
                } else {
                    leftIconClickListener?.invoke(v)
                }
            }
            v == binding.btRight -> {
                rightButtonClickListener?.invoke(v)
            }
        }
    }

    fun setOnLeftIconClickListener(leftIconClickListener: (v: View?) -> Unit) {
        this.leftIconClickListener = leftIconClickListener
    }

}