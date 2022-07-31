package com.growatt.atess.ui.plant.viewmodel

import android.text.TextUtils
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.plant.SearchType

class SearchViewModel : BaseViewModel() {

    companion object {
        const val KEY_PLANT_SEARCH_RECENT_WORD_LIST = "key_plant_search_recent_word_list"
        const val KEY_DEVICE_SEARCH_RECENT_WORD_LIST = "key_device_search_recent_word_list"

        /**
         * 分隔符
         */
        const val DELIMITER = "#"

        /**
         * 限制搜索关键词显示的最大数量
         */
        const val MAX_LENGTH = 10
    }

    fun getRecentWordList(@SearchType searchType: Int): List<String> {
        val wordListStr = when (searchType) {
            SearchType.PLANT -> {
                storageService().getString(KEY_PLANT_SEARCH_RECENT_WORD_LIST, null)
            }
            SearchType.DEVICE -> {
                storageService().getString(KEY_DEVICE_SEARCH_RECENT_WORD_LIST, null)
            }
            else -> ""
        }
        if (TextUtils.isEmpty(wordListStr)) {
            return emptyList()
        }
        return wordListStr?.split(DELIMITER) ?: emptyList()
    }

    /**
     * 添加搜索关键词，返回搜索词列表
     */
    fun addRecentWord(@SearchType searchType: Int, recentWord: String): List<String> {
        val recentWordList = getRecentWordList(searchType).toMutableList()
        if (recentWordList.contains(recentWord)) {
            recentWordList.remove(recentWord)
        }
        if (recentWordList.size == MAX_LENGTH) {
            recentWordList.removeLast()
        }
        recentWordList.add(0, recentWord)
        saveRecentWordList(searchType, recentWordList)
        return recentWordList
    }

    private fun saveRecentWordList(
        @SearchType searchType: Int,
        recentWordList: List<String>
    ) {
        when (searchType) {
            SearchType.PLANT -> {
                storageService().put(
                    KEY_PLANT_SEARCH_RECENT_WORD_LIST,
                    recentWordList.joinToString(separator = DELIMITER)
                )
            }
            SearchType.DEVICE -> {
                storageService().put(
                    KEY_DEVICE_SEARCH_RECENT_WORD_LIST,
                    recentWordList.joinToString(separator = DELIMITER)
                )
            }
        }
    }

    fun clear(@SearchType searchType: Int) {
        when (searchType) {
            SearchType.PLANT -> {
                storageService().put(KEY_PLANT_SEARCH_RECENT_WORD_LIST, null)
            }
            SearchType.DEVICE -> {
                storageService().put(KEY_DEVICE_SEARCH_RECENT_WORD_LIST, null)
            }
        }
    }
}