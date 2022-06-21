package com.growatt.lib.service.device

import java.util.*

/**
 * 语言
 */
enum class Language(val code: Int, val languageName: String, val locale: Locale) {

    SIMPLIFIED_CHINESE(0, "简体中文", Locale.SIMPLIFIED_CHINESE),
    English(1, "English", Locale.TRADITIONAL_CHINESE);

    companion object {
        fun languageNames(): Array<String> {
            val list = ArrayList<String>()
            for (language in values()) {
                list.add(language.languageName)
            }
            return list.toTypedArray()
        }

        fun fromCode(code: Int): Language {
            for (language in values()) {
                if (language.code == code) {
                    return language
                }
            }
            return SIMPLIFIED_CHINESE
        }
    }

}