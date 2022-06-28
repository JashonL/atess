package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.ViewModel

/**
 * 注册
 */
class RegisterViewModel : ViewModel() {

    /**
     * 是否同意隐私政策
     */
    var isAgree = false

    var selectArea: String? = null

    fun isChina(): Boolean {
        return true
    }

}