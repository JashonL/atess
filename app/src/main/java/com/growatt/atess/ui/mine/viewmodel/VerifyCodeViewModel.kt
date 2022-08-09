package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.atess.ui.mine.fragment.RegisterAccountType
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpErrorModel
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 验证码相关
 * 1.获取验证码
 * 2.验证验证码
 */
class VerifyCodeViewModel : BaseViewModel() {

    val getVerifyCodeLiveData = MutableLiveData<Pair<Int, String?>>()
    val verifyCodeLiveData = MutableLiveData<String>()

    /**
     * 获取验证码
     * @param phoneOrEmailStr 手机号或者邮箱
     */
    fun fetchVerifyCode(phoneOrEmailStr: String, @RegisterAccountType type: Int) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("phoneOrEmailStr", phoneOrEmailStr)
                if (type == RegisterAccountType.PHONE) {
                    put("areaCode", "86")
                }
            }
            apiService().httpGet(ApiPath.Mine.GET_VERIFY_CODE, params, object :
                HttpCallback<HttpResult<String>>() {
                override fun success(result: HttpResult<String>) {
                    if (result.isBusinessSuccess()) {
                        val remainingTime = (result.data ?: "0").toInt()
                        getVerifyCodeLiveData.value = Pair(remainingTime, null)
                    } else {
                        getVerifyCodeLiveData.value = Pair(0, result.msg ?: "")
                    }
                }

                override fun onFailure(errorModel: HttpErrorModel) {
                    getVerifyCodeLiveData.value = Pair(0, errorModel.errorMsg ?: "")
                }

            })
        }
    }

    /**
     * 校验验证码
     */
    fun verifyCode(phoneOrEmailStr: String, verifyCode: String) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("findbackStr", phoneOrEmailStr)
                put("code", verifyCode)
            }
            apiService().httpGet(ApiPath.Mine.VERIFY_CODE, params, object :
                HttpCallback<HttpResult<String>>() {
                override fun success(result: HttpResult<String>) {
                    if (result.isBusinessSuccess()) {
                        verifyCodeLiveData.value = null
                    } else {
                        verifyCodeLiveData.value = result.msg ?: ""
                    }
                }

                override fun onFailure(errorModel: HttpErrorModel) {
                    verifyCodeLiveData.value = errorModel.errorMsg ?: ""
                }

            })
        }
    }

}