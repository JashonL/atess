package com.growatt.atess.ui.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IntDef
import com.growatt.atess.R
import com.growatt.atess.databinding.HomeBottomViewBinding

/**
 * 自定义组合View-首页底部布局
 */
class HomeBottomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val binding: HomeBottomViewBinding

    private var homeTabSelectCallback: ((Int) -> Unit)? = null

    @HomeTab
    private var selectHomeTab = HomeTab.SYNOPSIS

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.home_bottom_view, this)
        binding = HomeBottomViewBinding.bind(view)
        setListener()
        updateView()
    }

    private fun setListener() {
        binding.tabSynopsis.setOnClickListener(this)
        binding.tabPlant.setOnClickListener(this)
        binding.tabService.setOnClickListener(this)
        binding.tabMine.setOnClickListener(this)
    }

    private fun updateView() {
        binding.tabSynopsis.setSelect(selectHomeTab == HomeTab.SYNOPSIS)
        binding.tabPlant.setSelect(selectHomeTab == HomeTab.PLANT)
        binding.tabService.setSelect(selectHomeTab == HomeTab.SERVICE)
        binding.tabMine.setSelect(selectHomeTab == HomeTab.MINE)
    }

    fun setSelectPosition(@HomeTab selectTab: Int) {
        if (this.selectHomeTab == selectTab) {
            return
        }
        this.selectHomeTab = selectTab
        homeTabSelectCallback?.invoke(selectTab)
        updateView()
    }

    fun showRedPoint(@HomeTab tab: Int, redPointText: String?) {
        getTabView(tab).showRedPoint(redPointText)
    }

    fun hideRedPoint(@HomeTab tab: Int) {
        getTabView(tab).hideRedPoint()
    }

    private fun getTabView(@HomeTab tab: Int): HomeTabView {
        return when (tab) {
            HomeTab.PLANT -> binding.tabPlant
            HomeTab.SERVICE -> binding.tabService
            HomeTab.MINE -> binding.tabMine
            else -> binding.tabSynopsis
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tabSynopsis -> setSelectPosition(HomeTab.SYNOPSIS)
            v === binding.tabPlant -> setSelectPosition(HomeTab.PLANT)
            v === binding.tabService -> setSelectPosition(HomeTab.SERVICE)
            v === binding.tabMine -> setSelectPosition(HomeTab.MINE)
        }
    }

    fun setOnTabSelectListener(tabSelectCallback: (homeTab: Int) -> Unit) {
        this.homeTabSelectCallback = tabSelectCallback
    }
}

@IntDef(HomeTab.SYNOPSIS, HomeTab.PLANT, HomeTab.SERVICE, HomeTab.MINE)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class HomeTab {
    companion object {
        const val SYNOPSIS = 0
        const val PLANT = 1
        const val SERVICE = 2
        const val MINE = 3
    }
}
