package com.growatt.atess.service.http

import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.growatt.lib.service.http.IHttpCallback
import com.growatt.lib.service.http.IHttpService
import com.growatt.lib.util.GsonManager
import com.growatt.lib.util.LogUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Okhttp处理器
 */
class OkhttpService : IHttpService() {

    companion object {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val FILE = "application/octet-stream".toMediaType()
        const val TAG = "http_respone"
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor(HttpLoggingInterceptor {
            LogUtil.i(TAG, it)
        }.also {
            it.setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun host(): String {
        return ApiPath.SERVER_HOST
    }

    override fun postForm(urlOrApi: String, params: Map<String, String>, callback: IHttpCallback) {
        val formBodyBuilder = FormBody.Builder()
        for (param in params) {
            formBodyBuilder.add(param.key, param.value)
        }

        val requestBuilder = Request.Builder()
        for (head in generateHeads()) {
            requestBuilder.addHeader(head.key, head.value)
        }
        val request =
            requestBuilder.url(generateUrl(urlOrApi)).post(formBodyBuilder.build()).build()
        callServer(request, callback)
    }

    override fun postJson(urlOrApi: String, params: Map<String, String>, callback: IHttpCallback) {
        val requestBody = GsonManager.toJson(params).toRequestBody(JSON)

        val requestBuilder = Request.Builder()
        for (head in generateHeads()) {
            requestBuilder.addHeader(head.key, head.value)
        }
        val request =
            requestBuilder.url(generateUrl(urlOrApi)).post(requestBody).build()
        callServer(request, callback)
    }

    override fun postFile(
        urlOrApi: String,
        params: Map<String, String>,
        file: File?,
        callback: IHttpCallback
    ) {
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (param in params) {
            multipartBodyBuilder.addFormDataPart(param.key, param.value)
        }
        file?.let {
            val requestBody = it.asRequestBody(FILE)
            multipartBodyBuilder.addFormDataPart("file", it.name, requestBody)
        }
        val requestBuilder = Request.Builder()
        for (head in generateHeads()) {
            requestBuilder.addHeader(head.key, head.value)
        }
        val request =
            requestBuilder.url(generateUrl(urlOrApi)).post(multipartBodyBuilder.build()).build()
        callServer(request, callback)
    }


    override fun httpGet(
        urlOrApi: String,
        params: Map<String, String>,
        callback: IHttpCallback
    ) {
        val uriBuilder = Uri.parse(generateUrl(urlOrApi)).buildUpon()
        for (param in params) {
            uriBuilder.appendQueryParameter(param.key, param.value)
        }

        val requestBuilder = Request.Builder()
        for (head in generateHeads()) {
            requestBuilder.addHeader(head.key, head.value)
        }
        val request = requestBuilder.url(uriBuilder.toString()).build()
        callServer(request, callback)
    }

    private fun callServer(request: Request, callback: IHttpCallback) {
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    callback.onFailure(e.message)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                mainHandler.post {
                    if (response.isSuccessful) {
                        callback.onSuccess(result)
                    } else {
                        callback.onFailure(result)
                    }
                }
            }
        })
    }
}