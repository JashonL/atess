package com.growatt.atess.service.http

object ApiPath {

    const val SERVER_HOST = "http://20.60.5.236:8089/"

    object Mine {
        /**
         * 登录
         */
        const val LOGIN = "ShineServer/ATSLogin"

        /**
         * 登出
         */
        const val LOGOUT = "ShineServer/ATSLogout"

        /**
         * 注册
         */
        const val REGISTER = "ShineServer/ATSregister/reHome"

        /**
         * 通过邮箱或手机号发送验证码
         */
        const val GET_VERIFY_CODE = "ShineServer/ATSregister/sendValidCode"

        /**
         * 获取国家列表
         */
        const val GET_COUNTRY_LIST = "ShineServer/ATSregister/getCountryList"

        /**
         * 校验验证码
         */
        const val VERIFY_CODE = "ShineServer/ATSregister/validCode"

        /**
         * 获取用户头像
         */
        const val GET_USER_AVATAR = "ShineServer/ATSSetting/getUserIcon"

        /**
         * 找回密码-修改密码
         */
        const val MODIFY_PASSWORD_BY_PHONE_OR_EMAIL =
            "ShineServer/ATSregister/changePasswordByPhoneOrEmail"

        /**
         * 设置-修改密码
         */
        const val MODIFY_PASSWORD = "ShineServer/ATSSetting/changePWD"

        /**
         * 更换邮箱
         */
        const val CHANGE_EMAIL = "ShineServer/ATSSetting/updateEmail"

        /**
         * 更换手机号
         */
        const val CHANGE_PHONE = "ShineServer/ATSSetting/updatePhoneNum"

        /**
         * 修改安装商编号
         */
        const val MODIFY_INSTALLER_NO = "ShineServer/ATSSetting/updateAgentCode"

        /**
         * 注销账号
         */
        const val CANCEL_ACCOUNT = "ShineServer/ATSSetting/eraseUser"

        /**
         * 上传用户头像
         */
        const val UPLOAD_USER_ICON = "ShineServer/ATSSetting/uploadUserIcon"

    }

    object Plant {
        /**
         * 货币列表
         */
        const val CURRENCY_LIST = "ShineServer/ATSregister/moneyUnitList"

        /**
         * 根据国家来获取时区列表
         */
        const val GET_TIMEZONE_BY_COUNTRY = "ShineServer/ATSregister/getTimezoneByCountry"

        /**
         * 添加采集器
         */
        const val ADD_COLLECTOR = "ShineServer/ATSplant/addDevice"
    }
}