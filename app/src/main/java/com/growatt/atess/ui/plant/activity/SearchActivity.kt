package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivitySearchBinding
import com.growatt.atess.model.plant.SearchType
import com.growatt.atess.ui.plant.fragment.DeviceTabFragment
import com.growatt.atess.ui.plant.fragment.PlantTabFragment
import com.growatt.atess.ui.plant.viewmodel.SearchViewModel
import com.growatt.lib.util.KeyBoardUtil
import com.growatt.lib.util.ViewUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 搜索页面
 */
class SearchActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_SEARCH_TYPE = "key_search_type"
        private const val KEY_PLANT_ID = "key_plant_id"

        fun startPlantSearch(context: Context?) {
            context?.startActivity(Intent(context, SearchActivity::class.java).also {
                it.putExtra(KEY_SEARCH_TYPE, SearchType.PLANT)
            })
        }

        fun startDeviceSearch(context: Context?, plantId: String?) {
            context?.startActivity(Intent(context, SearchActivity::class.java).also {
                it.putExtra(KEY_SEARCH_TYPE, SearchType.DEVICE)
                it.putExtra(KEY_PLANT_ID, plantId)
            })
        }

    }

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private var plantId: String? = null

    private val searchType: Int by lazy(LazyThreadSafetyMode.NONE) {
        intent.getIntExtra(KEY_SEARCH_TYPE, SearchType.PLANT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        plantId = intent.getStringExtra(KEY_PLANT_ID)
    }

    private fun setListener() {
        binding.tvCancel.setOnClickListener(this)
        binding.ivCleatEmpty.setOnClickListener(this)
        binding.etSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                refreshRecentWordView(viewModel.getRecentWordList(searchType))
                supportFragmentManager.findFragmentById(R.id.fragment_search_result)?.also {
                    supportFragmentManager.commit(true) {
                        remove(it)
                    }
                }
            }
        }
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchWord = binding.etSearch.text.toString().trim()
                if (TextUtils.isEmpty(searchWord)) {

                } else {
                    refreshRecentWordView(viewModel.addRecentWord(searchType, searchWord))
                    search(searchWord)
                }
                true
            }
            false
        }
    }

    private fun search(searchWord: String) {
        binding.etSearch.clearFocus()
        KeyBoardUtil.hideInput(this)
        binding.clContainer.gone()
        supportFragmentManager.commit(true) {
            when (searchType) {
                SearchType.PLANT -> add(R.id.fragment_search_result, PlantTabFragment(searchWord))
                SearchType.DEVICE -> add(
                    R.id.fragment_search_result,
                    DeviceTabFragment(plantId, searchWord)
                )
            }
        }
    }

    private fun initView() {
        val recentWordList = viewModel.getRecentWordList(searchType)
        refreshRecentWordView(recentWordList)
        when (searchType) {
            SearchType.PLANT -> binding.etSearch.setHint(R.string.please_input_plant_or_sn)
            SearchType.DEVICE -> binding.etSearch.setHint(R.string.please_input_device_sn_or_alias)
        }
    }

    private fun refreshRecentWordView(recentWordList: List<String>) {
        if (recentWordList.isEmpty()) {
            binding.clContainer.gone()
        } else {
            binding.clContainer.visible()
            binding.flexbox.removeAllViews()
            for (word in recentWordList) {
                binding.flexbox.addView(generateItemView(word))
            }
        }
    }

    private fun generateItemView(word: String): View {
        return TextView(this).also {
            it.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            it.background = ViewUtil.createShape(resources.getColor(R.color.color_05000000), 2)
            it.gravity = Gravity.CENTER
            it.setTextColor(resources.getColor(R.color.text_gray_bb))
            it.setPadding(
                ViewUtil.dp2px(this, 10f),
                ViewUtil.dp2px(this, 2f),
                ViewUtil.dp2px(this, 10f),
                ViewUtil.dp2px(this, 2f)
            )
            it.textSize = 12f
            it.text = word
            it.setOnClickListener {
                refreshRecentWordView(viewModel.addRecentWord(searchType, word))
                search(word)
                binding.etSearch.setText(word)
            }
        }

    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvCancel -> finish()
            v === binding.ivCleatEmpty -> {
                viewModel.clear(searchType)
                binding.clContainer.gone()
            }
        }
    }
}

