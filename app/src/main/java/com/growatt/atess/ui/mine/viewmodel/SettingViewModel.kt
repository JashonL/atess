package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.service.http.ApiPath
import com.growatt.atess.ui.mine.fragment.RegisterAccountType
import com.growatt.lib.base.BaseViewModel
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.util.MD5Util
import kotlinx.coroutines.launch

/**
 * 选择国家/地区
 */
class SettingViewModel : BaseViewModel() {

    val userAvatarLiveData = MutableLiveData<Pair<String?, String?>>()
    val logoutLiveData = MutableLiveData<String?>()
    val modifyPasswordLiveData = MutableLiveData<String?>()
    val changePhoneOrEmailLiveData = MutableLiveData<String?>()

    /**
     * 获取头像
     */
    fun fetchUserAvatar() {
        viewModelScope.launch {
            apiService().httpGet(
                ApiPath.Mine.GET_USER_AVATAR,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            val userAvatar = result.data
                            userAvatarLiveData.value = Pair(userAvatar, null)
                        } else {
                            userAvatarLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        userAvatarLiveData.value = Pair(null, error ?: "")
                    }
                })
        }
    }

    /**
     * 登出
     */
    fun logout() {
        viewModelScope.launch {
            apiService().httpGet(
                ApiPath.Mine.LOGOUT,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            logoutLiveData.value = null
                        } else {
                            logoutLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        logoutLiveData.value = error ?: ""
                    }
                })
        }
    }

    /**
     * 设置-修改密码
     */
    fun modifyPassword(oldPassword: String, newPassword: String) {
        val params = hashMapOf<String, String>().apply {
            put("OldPWD", MD5Util.md5(oldPassword) ?: "")
            put("NewPWD", MD5Util.md5(newPassword) ?: "")
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

    /**
     * 设置-修改邮箱或者手机号
     */
    fun changePhoneOrEmail(phoneOrEmail: String, @RegisterAccountType registerAccountType: Int) {
        val params = hashMapOf<String, String>().apply {
            if (registerAccountType == RegisterAccountType.PHONE) {
                put("Email", phoneOrEmail)
            } else {
                put("OldPWD", phoneOrEmail)
            }
        }

        val requestApi =
            if (registerAccountType == RegisterAccountType.PHONE) ApiPath.Mine.CHANGE_PHONE else ApiPath.Mine.CHANGE_EMAIL

        viewModelScope.launch {
            apiService().postForm(
                requestApi, params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            changePhoneOrEmailLiveData.value = null
                        } else {
                            changePhoneOrEmailLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        changePhoneOrEmailLiveData.value = error ?: ""
                    }
                })
        }
    }
}