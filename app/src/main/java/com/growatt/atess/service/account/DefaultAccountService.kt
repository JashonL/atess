package com.growatt.atess.service.account

import android.content.Context
import com.growatt.atess.application.Foreground
import com.growatt.atess.ui.mine.activity.LoginActivity
import com.growatt.lib.service.account.BaseAccountService

class DefaultAccountService : BaseAccountService() {

    override fun login(context: Context) {
        LoginActivity.startClearTask(context)
    }

    override fun tokenExpired() {
        logout()
        Foreground.instance().getTopActivity()?.also {
            login(it)
        }
    }
}