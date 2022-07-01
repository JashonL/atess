package com.growatt.atess.ui.mine.viewmodel

import android.text.TextUtils
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
 * 注册
 */
class RegisterViewModel : BaseViewModel() {

    val registerLiveData = MutableLiveData<String>()

    /**
     * 是否同意隐私政策
     */
    var isAgree = false

    var selectArea: String = ""

    var phone: String = ""

    var email: String = ""

    fun isChina(): Boolean {
        return "中国" == selectArea || "China" == selectArea
    }

    fun getRequirePhoneOrEmail(): String {
        return if (isChina()) phone else email
    }

    fun getRegisterAccountType(): Int {
        return if (isChina()) RegisterAccountType.PHONE else RegisterAccountType.EMAIL
    }

    /**
     * 注册
     */
    fun register(userName: String, password: String, agentCode: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("country", selectArea)
                put("userName", userName)
                put("password", MD5Util.md5(password) ?: "")
                if (!TextUtils.isEmpty(phone)) {
                    put("phoneNum", phone)
                }
                if (!TextUtils.isEmpty(email)) {
                    put("email", email)
                }
                if (!TextUtils.isEmpty(agentCode)) {
                    put("agentCode", agentCode)
                }
            }
            apiService().postJson(ApiPath.Mine.REGISTER, params, object :
                HttpCallback<HttpResult<String>>() {
                override fun success(result: HttpResult<String>) {
                    if (result.isBusinessSuccess()) {
                        registerLiveData.value = null
                    } else {
                        registerLiveData.value = result.msg ?: ""
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    registerLiveData.value = error ?: ""
                }
            })
        }
    }

}