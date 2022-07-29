package com.growatt.atess.service.http

object ApiPath {

    const val SERVER_HOST = "http://20.60.5.236:8089/"//开发本地服务器
//const val SERVER_HOST = "http://47.243.120.111/"//测试服务器

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

        /**
         * 修改电站
         */
        const val UPDATE_PLANT = "ATSplant/updatePlant"

        /**
         * 通过采集器序列号获取校验码
         */
        const val GET_CHECK_CODE = "ATSplant/getValidCodeBySN"

        /**
         * 获取电站列表
         */
        const val GET_PLANT_LIST = "ATSplant/getAllPlantList"

        /**
         * 删除电站
         */
        const val DELETE_PLANT = "ATSplant/delplant"

        /**
         * 获取电站详情
         */
        const val GET_PLANT_INFO = "ATSplant/getUserCenterEnertyDataByPlantid"

        /**
         * 我的设备列表
         */
        const val GET_DEVICE_LIST = "ATSDevice/getDevices"

        /**
         * 电站详情-PCS和HPS设备序列号
         */
        const val GET_PCS_AND_HPS_LIST = "ATSplant/getPCSAndHPSList"

        /**
         * HPS设备详情
         */
        const val GET_DEVICE_HPS_INFO = "ATSDevice/getHPSBySn"

        /**
         * HPS图表详情
         */
        const val GET_DEVICE_HPS_CHART_INFO = "ATSDevice/getHPSDataList"

        /**
         * PCS设备详情
         */
        const val GET_DEVICE_PCS_INFO = "ATSDevice/getPCSBySn"

        /**
         * PCS图表详情
         */
        const val GET_DEVICE_PCS_CHART_INFO = "ATSDevice/getPCSDataList"

        /**
         * PBD设备详情
         */
        const val GET_DEVICE_PBD_INFO = "ATSDevice/getPBDBySn"

        /**
         * PBD图表详情
         */
        const val GET_DEVICE_PBD_CHART_INFO = "ATSDevice/getPBDDataList"

        /**
         * 获取BMS、MBMS、BCU_BMS设备详情
         */
        const val GET_DEVICE_BMS_INFO = "ATSDevice/getBMSBySn"

        /**
         * 获取BMS、MBMS、BCU_BMS图表详情
         */
        const val GET_DEVICE_BMS_CHART_INFO = "ATSDevice/getBMSDataList"

        /**
         * HPS或PCS能源概况
         */
        const val GET_ENERGY_INFO = "ATSDevice/getEnergyOverview"

        /**
         * 电站详情获取HPS与PCS设备图表详情
         */
        const val GET_HPS_OR_PCS_CHART_INFO = "ATSDevice/getHpsOrPcsChartData"

        /**
         * 获取HPS系统运行图
         */
        const val GET_HPS_SYSTEM_OPERATION = "ATSDevice/getHPSRunChart"

    }
}