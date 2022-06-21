package com.growatt.lib.service.http

class HttpResult<T> {
    var code = 0
    var data: T? = null
    var error: String? = null
}
