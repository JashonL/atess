package com.growatt.lib.service.account

import android.text.TextUtils
import com.growatt.lib.LibApplication
import com.growatt.lib.service.storage.DefaultStorageService
import com.growatt.lib.util.GsonManager

abstract class BaseAccountService : IAccountService {

    companion object {
        private const val KEY_TOKEN = "key_token"
        private const val KEY_USER = "key_user"
    }

    private var token: String? = null
    private var user: User? = null
    private val listeners = mutableSetOf<IAccountService.AccountListener>()

    private val userStorage by lazy {
        DefaultStorageService(LibApplication.instance(), "account")
    }

    init {
        token = userStorage.getString(KEY_TOKEN, "")
        userStorage.getString(KEY_USER, "")?.let {
            user = GsonManager.fromJson(it, User::class.java)
        }
    }

    override fun token(): String? {
        return token
    }

    override fun saveToken(token: String?) {
        userStorage.put(KEY_TOKEN, token)
    }

    override fun saveUserInfo(user: User?) {
        userStorage.put(KEY_USER, GsonManager.toJson(user))
        this.user = user
        dispatchAccountChanged()
    }

    override fun logout() {
        userStorage.clear()
        token = null
        user = null
        dispatchAccountChanged()
    }

    override fun dispatchAccountChanged() {
        for (listener in listeners) {
            listener.onAccountChange(this)
        }
    }

    override fun id(): String? {
        return user?.id
    }

    override fun isLogin(): Boolean {
        return !TextUtils.isEmpty(token)
    }

    override fun addListener(listener: IAccountService.AccountListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: IAccountService.AccountListener) {
        listeners.remove(listener)
    }

}