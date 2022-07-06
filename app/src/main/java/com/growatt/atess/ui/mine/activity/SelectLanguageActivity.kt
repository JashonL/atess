package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.base.BaseViewHolder
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.ActivitySelectLanguageBinding
import com.growatt.atess.databinding.LanguageViewHolderBinding
import com.growatt.lib.service.device.Language
import com.growatt.lib.util.Util
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 选择语言页面
 */
class SelectLanguageActivity : BaseActivity() {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, SelectLanguageActivity::class.java))
        }

    }

    private lateinit var binding: ActivitySelectLanguageBinding

    //支持的语言列表
    private val supportLanguageList = arrayOf(
        Pair(MainApplication.instance().getString(R.string.follow_system), Language.FOLLOW_SYSTEM),
        Pair("中文", Language.SIMPLIFIED_CHINESE),
        Pair("繁體中文", Language.TRADITIONAL_CHINESE),
        Pair("English", Language.ENGLISH),
        Pair("Français", Language.FRENCH),
        Pair("Greek", Language.GREEK),
        Pair("German", Language.GERMAN),
        Pair("Nederland", Language.NEDERLAND),
        Pair("Italiano", Language.ITALIANO),
        Pair("日本語", Language.JAPANESE),
        Pair("Polish", Language.POLISH),
        Pair("Português", Language.PORTUGUESE),
        Pair("Español", Language.SPANISH),
        Pair("Türkçe", Language.TURKISH),
        Pair("Vietnamese", Language.VIETNAMESE),
        Pair("한국어", Language.KOREAN),
        Pair("ไทย", Language.THAI)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListener()
    }

    private fun setListener() {
        binding.title.setOnRightButtonClickListener {
            val selectLanguage = (binding.rvList.adapter as Adapter).getSelectLanguage()
            if (selectLanguage != deviceService().getAppLanguage()) {
                lifecycleScope.launch {
                    deviceService().setAppLanguage(selectLanguage)
                    Util.backToDesktop(this@SelectLanguageActivity)
                    delay(1000)
                    Util.restartApp(this@SelectLanguageActivity)
                }
            }
        }
    }

    private fun initView() {
        binding.rvList.adapter = Adapter()
    }

    inner class Adapter : RecyclerView.Adapter<LanguageViewHolder>(), OnItemClickListener {

        private var selectPosition = 0

        init {
            val language = deviceService().getAppLanguage()
            for (position in supportLanguageList.indices) {
                if (supportLanguageList[position].second == language) {
                    selectPosition = position
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
            return LanguageViewHolder.create(parent, this)
        }

        override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
            holder.bindData(supportLanguageList[position].first, selectPosition == position)
        }

        override fun getItemCount(): Int {
            return supportLanguageList.size
        }

        override fun onItemClick(v: View?, position: Int) {
            refreshSelectPosition(position)
        }

        private fun refreshSelectPosition(selectPosition: Int) {
            if (this.selectPosition == selectPosition) {
                return
            }
            val oldSelectPosition = this.selectPosition
            this.selectPosition = selectPosition

            notifyItemChanged(oldSelectPosition)
            notifyItemChanged(selectPosition)
        }

        fun getSelectLanguage(): Language {
            return supportLanguageList[selectPosition].second
        }

    }

    class LanguageViewHolder(
        itemView: View,
        private var onItemClickListener: OnItemClickListener? = null
    ) :
        BaseViewHolder(itemView) {
        lateinit var binding: LanguageViewHolderBinding

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClickListener: OnItemClickListener? = null
            ): LanguageViewHolder {
                val binding =
                    LanguageViewHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                val viewHolder =
                    LanguageViewHolder(binding.root, onItemClickListener)
                viewHolder.binding = binding
                viewHolder.onItemClickListener = onItemClickListener
                viewHolder.binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick(it, viewHolder.bindingAdapterPosition)
                }
                return viewHolder
            }
        }

        fun bindData(language: String?, isSelect: Boolean) {
            if (isSelect) {
                binding.tvLanguage.setTextColor(MainApplication.instance().resources.getColor(R.color.text_red))
            } else {
                binding.tvLanguage.setTextColor(MainApplication.instance().resources.getColor(R.color.text_black))
            }
            binding.tvLanguage.text = language
            binding.root.tag = language
        }

    }

}