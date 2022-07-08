package com.growatt.atess.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import com.growatt.lib.LibApplication
import com.growatt.lib.R
import com.growatt.lib.service.ServiceManager
import com.growatt.lib.service.account.IAccountService
import com.growatt.lib.service.device.IDeviceService
import com.growatt.lib.service.device.Language
import com.growatt.lib.service.http.IHttpService
import com.growatt.lib.service.location.ILocationService
import com.growatt.lib.service.storage.IStorageService

abstract class BaseActivity : AppCompatActivity(), ServiceManager.ServiceInterface, ViewHelper {

    private val progressDialog by lazy(LazyThreadSafetyMode.NONE) {
        ProgressDialog(this).apply {
            setCanceledOnTouchOutside(false)
            setMessage(getString(R.string.loading))
        }
    }

    /**
     * 多语言初始化
     */
    override fun attachBaseContext(newBase: Context?) {
        val appLanguage = LibApplication.instance().deviceService().getAppLanguage()
        if (appLanguage == Language.FOLLOW_SYSTEM) {
            super.attachBaseContext(newBase)
            return
        }

        super.attachBaseContext(newBase?.resources?.configuration?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setLocales(LocaleList(appLanguage.locale))
            } else {
                setLocale(appLanguage.locale)
            }
        }?.let { newBase.createConfigurationContext(it) })
    }

    override fun apiService(): IHttpService {
        return LibApplication.instance().apiService()
    }

    override fun storageService(): IStorageService {
        return LibApplication.instance().storageService()
    }

    override fun deviceService(): IDeviceService {
        return LibApplication.instance().deviceService()
    }

    override fun accountService(): IAccountService {
        return LibApplication.instance().accountService()
    }

    override fun locationService(): ILocationService {
        return LibApplication.instance().locationService()
    }

    override fun showDialog() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    override fun dismissDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

}