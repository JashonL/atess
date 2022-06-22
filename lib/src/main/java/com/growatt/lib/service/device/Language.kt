package com.growatt.lib.service.device

import android.content.res.Resources
import android.os.Build
import java.util.*

/**
 * 语言
 */
enum class Language(val code: Int, val languageName: String, val locale: Locale) {

    SYSTEM_DEFAULT(-1, "SYSTEM_DEFAULT", Locale.ENGLISH),
    SIMPLIFIED_CHINESE(0, "简体中文", Locale.SIMPLIFIED_CHINESE),
    English(1, "English", Locale.ENGLISH);


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
            return SYSTEM_DEFAULT
        }

        fun fromLocale(locale: Locale): Language {
            for (language in values()) {
                if (language.locale == locale) {
                    return language
                }
            }
            return SYSTEM_DEFAULT
        }

        /**
         * 获取系统默认的语言
         */
        fun getLocale(): Locale {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Resources.getSystem().configuration.locales[0] else Locale.getDefault()
        }
    }

}