package com.growatt.atess.service.http

object ApiPath {

    const val SERVER_HOST = "http://20.60.5.236:8089/"//开发本地服务器
//    const val SERVER_HOST = "http://47.243.120.111/"//测试服务器

    object Mine {
        /**
         * 登录
         */
        const val LOGIN = "ATSLogin"

        /**
         * 登出
         */
        const val LOGOUT = "ATSLogout"

        /**
         * 注册
         */
        const val REGISTER = "ATSregister/reHome"

        /**
         * 通过邮箱或手机号发送验证码
         */
        const val GET_VERIFY_CODE = "ATSregister/sendValidCode"

        /**
         * 获取国家列表
         */
        const val GET_COUNTRY_LIST = "ATSregister/getCountryList"

        /**
         * 校验验证码
         */
        const val VERIFY_CODE = "ATSregister/validCode"

        /**
         * 获取用户头像
         */
        const val GET_USER_AVATAR = "ATSSetting/getUserIcon"

        /**
         * 找回密码-修改密码
         */
        const val MODIFY_PASSWORD_BY_PHONE_OR_EMAIL =
            "ATSregister/changePasswordByPhoneOrEmail"

        /**
         * 设置-修改密码
         */
        const val MODIFY_PASSWORD = "ATSSetting/changePWD"

        /**
         * 更换邮箱
         */
        const val CHANGE_EMAIL = "ATSSetting/updateEmail"

        /**
         * 更换手机号
         */
        const val CHANGE_PHONE = "ATSSetting/updatePhoneNum"

        /**
         * 修改安装商编号
         */
        const val MODIFY_INSTALLER_NO = "ATSSetting/updateAgentCode"

        /**
         * 注销账号
         */
        const val CANCEL_ACCOUNT = "ATSSetting/eraseUser"

        /**
         * 上传用户头像
         */
        const val UPLOAD_USER_ICON = "ATSSetting/uploadUserIcon"

    }

    object Plant {
        /**
         * 货币列表
         */
        const val CURRENCY_LIST = "ATSregister/moneyUnitList"

        /**
         * 根据国家来获取时区列表
         */
        const val GET_TIMEZONE_BY_COUNTRY = "ATSregister/getTimezoneByCountry"

        /**
         * 城市列表
         */
        const val GET_CITY_LIST = "ATSregister/getProvAndCityList"

        /**
         * 添加采集器
         */
        const val ADD_COLLECTOR = "ATSplant/addDevice"

        /**
         * 添加电站
         */
        const val ADD_PLANT = "ATSplant/addPlant"
    }
}