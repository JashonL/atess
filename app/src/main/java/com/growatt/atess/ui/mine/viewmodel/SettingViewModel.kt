package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.atess.ui.mine.fragment.RegisterAccountType
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.util.MD5Util
import kotlinx.coroutines.launch
import java.io.File

/**
 * 选择国家/地区
 */
class SettingViewModel : BaseViewModel() {

    val userAvatarLiveData = MutableLiveData<Pair<String?, String?>>()
    val logoutLiveData = MutableLiveData<String?>()
    val modifyPasswordLiveData = MutableLiveData<String?>()
    val changePhoneOrEmailLiveData = MutableLiveData<String?>()
    val modifyInstallerNoLiveData = MutableLiveData<String?>()
    val cancelAccountLiveData = MutableLiveData<String?>()

    val uploadUserAvatarLiveData = MutableLiveData<Pair<String?, String?>>()

    /**
     * 获取头像
     */
    fun fetchUserAvatar() {
        viewModelScope.launch {
            apiService().post(
                ApiPath.Mine.GET_USER_AVATAR,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            val userAvatar = result.data
                            userAvatarLiveData.value = Pair(userAvatar, null)
                        } else {
                            userAvatarLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        userAvatarLiveData.value = Pair(null, error ?: "")
                    }
                })
        }
    }

    /**
     * 登出
     */
    fun logout() {
        viewModelScope.launch {
            apiService().post(
                ApiPath.Mine.LOGOUT,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            logoutLiveData.value = null
                        } else {
                            logoutLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        logoutLiveData.value = error ?: ""
                    }
                })
        }
    }

    /**
     * 设置-修改密码
     */
    fun modifyPassword(oldPassword: String, newPassword: String) {
        val params = hashMapOf<String, String>().apply {
            put("OldPWD", MD5Util.md5(oldPassword) ?: "")
            put("NewPWD", MD5Util.md5(newPassword) ?: "")
        }

        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Mine.MODIFY_PASSWORD, params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            modifyPasswordLiveData.value = null
                        } else {
                            modifyPasswordLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        modifyPasswordLiveData.value = error ?: ""
                    }
                })
        }
    }

    /**
     * 设置-修改邮箱或者手机号
     */
    fun changePhoneOrEmail(
        phoneOrEmail: String,
        @RegisterAccountType registerAccountType: Int,
        verifyCode: String
    ) {
        val params = hashMapOf<String, String>().apply {
            if (registerAccountType == RegisterAccountType.PHONE) {
                put("Email", phoneOrEmail)
            } else {
                put("OldPWD", phoneOrEmail)
            }
        }

        val requestApi =
            if (registerAccountType == RegisterAccountType.PHONE) ApiPath.Mine.CHANGE_PHONE else ApiPath.Mine.CHANGE_EMAIL

        viewModelScope.launch {
            apiService().postForm(
                requestApi, params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            changePhoneOrEmailLiveData.value = null
                        } else {
                            changePhoneOrEmailLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        changePhoneOrEmailLiveData.value = error ?: ""
                    }
                })
        }
    }

    /**
     * 设置-修改安装商编号
     */
    fun modifyInstallerNo(installerNo: String) {
        val params = hashMapOf<String, String>().apply {
            put("agentCode", installerNo)
        }
        viewModelScope.launch {
            apiService().postForm(
                ApiPath.Mine.MODIFY_INSTALLER_NO, params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            modifyInstallerNoLiveData.value = null
                        } else {
                            modifyInstallerNoLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        modifyInstallerNoLiveData.value = error ?: ""
                    }
                })
        }
    }

    /**
     * 设置-注销账号
     */
    fun cancelAccount() {
        viewModelScope.launch {
            apiService().post(
                ApiPath.Mine.CANCEL_ACCOUNT,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            cancelAccountLiveData.value = null
                        } else {
                            cancelAccountLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        cancelAccountLiveData.value = error ?: ""
                    }
                })
        }
    }

    /**
     * 设置-上传用户头像
     */
    fun uploadUserAvatar(filePath: String) {
        viewModelScope.launch {
            apiService().postFile(
                ApiPath.Mine.UPLOAD_USER_ICON, hashMapOf(), File(filePath),
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            uploadUserAvatarLiveData.value = Pair(result.data, null)
                        } else {
                            uploadUserAvatarLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        uploadUserAvatarLiveData.value = Pair(null, error ?: "")
                    }
                })
        }
    }
}