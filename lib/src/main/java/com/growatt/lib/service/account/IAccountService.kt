package com.growatt.lib.service.account

import android.content.Context
import com.growatt.lib.service.Service

interface IAccountService : Service {

    /**
     * 获取token
     */
    fun token(): String?

    fun saveToken(token: String?)

    fun saveUserInfo(user: User?)

    fun login(context: Context)

    fun logout()

    fun tokenExpired()

    fun id(): String?

    fun isLogin(): Boolean

    fun addListener(listener: AccountListener)

    fun removeListener(listener: AccountListener)

    fun dispatchAccountChanged()

    interface AccountListener {
        fun onAccountChange(account: IAccountService)
    }
}