package com.growatt.lib.service.http

import  com.growatt.lib.LibApplication
import com.growatt.lib.util.GsonManager
import com.growatt.lib.util.ToastUtil
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class HttpCallback<R> : IHttpCallback {

    override fun onSuccess(response: String?) {
        //统一处理登录失效的问题
        try {
            response?.also {
                val status_code = JSONObject(response).optString("status_code").toString()
                if (status_code == "90001") {
                    LibApplication.instance().accountService().tokenExpired()
                    val errorMsg = JSONObject(response).optString("msg").toString()
                    ToastUtil.show(errorMsg)
                    return
                }
            }
        } catch (e: JSONException) {
        }

        val result: R? = GsonManager.fromJsonType(response, getType())
        if (result == null) {
            onFailure("parsing exceptions")
        } else {
            success(result)
        }
    }

    abstract fun success(result: R)

    private fun getType(): Type {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments[0]
    }

    override fun onFailure(error: String?) {
    }
}