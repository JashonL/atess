package com.growatt.lib.service.http

/**
 * 网络响应基本字段
 */
data class HttpResult<T>(var status_code: String? = "0", var msg: String? = "", var data: T?)