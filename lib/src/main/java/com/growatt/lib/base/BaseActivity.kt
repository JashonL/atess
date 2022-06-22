package com.growatt.lib.base

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import com.growatt.lib.LibApplication
import com.growatt.lib.service.device.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    /**
     * 多语言初始化
     */
    override fun attachBaseContext(newBase: Context?) {
        val appLanguage = LibApplication.instance().deviceService().getAppLanguage()
        if (appLanguage == Language.SYSTEM_DEFAULT) {
            super.attachBaseContext(newBase)
            return
        }

        super.attachBaseContext(newBase?.resources?.configuration?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setLocales(LocaleList(appLanguage.locale))
            } else {
                setLocale(appLanguage.locale)
            }
        }?.let { createConfigurationContext(it) })
    }

    override fun onDestroy() {
        //取消协程，和生命周期绑定
        cancel()
        super.onDestroy()
    }

}