package com.growatt.atess.service.http

object ApiPath {

    val serverHostUrl = "http://20.60.5.236:8089/"

    object Mine {
        /**
         * 登录
         */
        val login = "ShineServer/ATSLogin"

        /**
         * 登出
         */
        val logout = "ShineServer/ATSLogout"

        /**
         * 注册
         */
        val register = "ShineServer/ATSregister/reHome"

        /**
         * 通过邮箱或手机号发送验证码
         */
        val getVerifyCode = "ShineServer/ATSregister/findbackPWD"

        /**
         * 获取国家列表
         */
        val getCountryList = "ShineServer/ATSregister/getCountryList"

        /**
         * 校验验证码
         */
        val verifyCode = "ShineServer/ATSregister/validCode"

    }
}