package com.growatt.atess.ui.mine.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.base.BaseViewModel
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.util.MD5Util
import kotlinx.coroutines.launch

/**
 * 注册
 */
class RegisterViewModel : BaseViewModel() {

    val getVerifyCodeLiveData = MutableLiveData<Pair<Int, String?>>()
    val verifyCodeLiveData = MutableLiveData<String>()
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
        val require = if (isChina()) phone else email
        return require
    }

    /**
     * 获取验证码
     * @param findbackStr 手机号或者邮箱
     * @param areaCode    手机的时候需要填写
     */
    fun fetchVerifyCode(areaCode: String = "86") {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("findbackStr", getRequirePhoneOrEmail())
                if (isChina()) {
                    put("areaCode", areaCode)
                }
            }
            apiService().httpGet(ApiPath.Mine.getVerifyCode, params, object :
                HttpCallback<HttpResult<String>>() {
                override fun success(result: HttpResult<String>) {
                    if (result.isBusinessSuccess()) {
                        val remainingTime = (result.data ?: "0").toInt()
                        getVerifyCodeLiveData.value = Pair(remainingTime, null)
                    } else {
                        getVerifyCodeLiveData.value = Pair(0, result.msg)
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    getVerifyCodeLiveData.value = Pair(0, error)
                }
            })
        }
    }

    /**
     * 校验验证码
     */
    fun verifyCode(verifyCode: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("findbackStr", getRequirePhoneOrEmail())
                put("code", verifyCode)
            }
            apiService().httpGet(ApiPath.Mine.verifyCode, params, object :
                HttpCallback<HttpResult<String>>() {
                override fun success(result: HttpResult<String>) {
                    if (result.isBusinessSuccess()) {
                        verifyCodeLiveData.value = null
                    } else {
                        verifyCodeLiveData.value = result.msg
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    verifyCodeLiveData.value = error
                }
            })
        }
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
            apiService().postForm(ApiPath.Mine.register, params, object :
                HttpCallback<HttpResult<String>>() {
                override fun success(result: HttpResult<String>) {
                    if (result.isBusinessSuccess()) {
                        registerLiveData.value = null
                    } else {
                        registerLiveData.value = result.msg
                    }
                }

                override fun onFailure(error: String?) {
                    super.onFailure(error)
                    registerLiveData.value = error
                }
            })
        }
    }

}