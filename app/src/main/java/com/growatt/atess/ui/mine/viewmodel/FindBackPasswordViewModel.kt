package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.util.MD5Util
import kotlinx.coroutines.launch

/**
 * 找回密码
 */
class FindBackPasswordViewModel : BaseViewModel() {

    val modifyPasswordLiveData = MutableLiveData<String?>()

    /**
     * 找回密码-修改密码
     */
    fun modifyPassword(phoneOrEmail: String, password: String, verifyCode: String?) {
        val params = hashMapOf<String, String>().apply {
            put("findStr", phoneOrEmail)
            put("password", MD5Util.md5(password) ?: "")
            put("validCode", verifyCode ?: "")
        }

        viewModelScope.launch {
            apiService().httpGet(
                ApiPath.Mine.MODIFY_PASSWORD_BY_PHONE_OR_EMAIL, params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            modifyPasswordLiveData.value = null
                        } else {
                            modifyPasswordLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        modifyPasswordLiveData.value = error ?: ""
                    }
                })
        }
    }
}