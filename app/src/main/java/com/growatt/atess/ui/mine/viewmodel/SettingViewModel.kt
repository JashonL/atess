package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.base.BaseViewModel
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 选择国家/地区
 */
class SettingViewModel : BaseViewModel() {

    val userAvatarLiveData = MutableLiveData<Pair<String?, String?>>()
    val logoutLiveData = MutableLiveData<String?>()

    /**
     * 获取头像
     */
    fun fetchUserAvatar() {
        viewModelScope.launch {
            apiService().httpGet(
                ApiPath.Mine.getUserAvatar,
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
                ApiPath.Mine.logout,
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
}