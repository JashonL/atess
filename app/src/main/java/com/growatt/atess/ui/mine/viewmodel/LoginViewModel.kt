package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.account.User
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpErrorModel
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.util.MD5Util
import kotlinx.coroutines.launch

/**
 * 登录
 */
class LoginViewModel : BaseViewModel() {

    val loginLiveData = MutableLiveData<Pair<User?, String?>>()

    /**
     * 登录
     */
    fun login(userName: String, password: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("userName", userName)
                put("password", MD5Util.md5(password) ?: "")
            }
            apiService().postForm(ApiPath.Mine.LOGIN, params, object :
                HttpCallback<HttpResult<User>>() {
                override fun success(result: HttpResult<User>) {
                    val user = result.data
                    if (result.isBusinessSuccess() && user != null) {
                        loginLiveData.value = Pair(user, null)
                    } else {
                        loginLiveData.value = Pair(null, result.msg ?: "")
                    }
                }

                override fun onFailure(errorModel: HttpErrorModel) {
                    loginLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                }

            })
        }
    }

}