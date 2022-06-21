package com.growatt.lib.service.http

interface IHttpCallback {

    fun onSuccess(response: String?)

    fun onFailure(error: String?)

}