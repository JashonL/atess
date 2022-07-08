package com.growatt.lib.service.location


data class LocationInfo(
    //纬度
    val latitude: Double,
    //经度
    val longitude: Double,
    //地址描述
    val address: String,
    //国家
    val country: String,
    //省
    val province: String,
    //城市
    val city: String,
    //城市编码
    val cityCode: String,
)