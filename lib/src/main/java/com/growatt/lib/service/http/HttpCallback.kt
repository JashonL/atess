package com.growatt.lib.service.http

import com.growatt.lib.util.GsonManager
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class HttpCallback<R> : IHttpCallback {

    override fun onSuccess(response: String?) {
        val result: R? = GsonManager.fromJsonType(response, getType())
        if (result == null) {
            onFailure("解析异常")
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