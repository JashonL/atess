package com.growatt.atess.application

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.os.Process
import com.amap.api.location.AMapLocationClient
import com.growatt.atess.R
import com.growatt.atess.service.account.DefaultAccountService
import com.growatt.atess.service.http.OkhttpService
import com.growatt.atess.service.location.AmapLocationService
import com.growatt.atess.ui.launch.fragment.UserAgreementDialog
import com.growatt.atess.ui.launch.monitor.UserAgreementMonitor
import com.growatt.lib.LibApplication
import com.growatt.lib.service.ServiceManager
import com.growatt.lib.service.ServiceType
import com.growatt.lib.service.account.IAccountService
import com.growatt.lib.service.device.DefaultDeviceService
import com.growatt.lib.service.device.IDeviceService
import com.growatt.lib.service.device.Language
import com.growatt.lib.service.http.IHttpService
import com.growatt.lib.service.location.ILocationService
import com.growatt.lib.service.storage.DefaultStorageService
import com.growatt.lib.service.storage.IStorageService
import com.growatt.lib.util.Util
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


class MainApplication : LibApplication() {

    companion object {
        private lateinit var instance: MainApplication
        fun instance() = instance

        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.colorAccent, android.R.color.white) //全局设置主题颜色
                MaterialHeader(context)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
    }

    private fun init() {
        //过滤掉其它进程
        if (!packageName.equals(Util.getProcessNameByPID(this, Process.myPid()))) {
            return
        }
        registerService()
        Foreground.init(this)

        if (isAgree()) {
            initSDK()
        } else {
            UserAgreementMonitor.watch { isAgree, monitor ->
                if (isAgree) {
                    //进行初始化，类似第三方库隐私政策相关的初始化
                    initSDK()
                }
                monitor.unWatch()
            }
        }

        initLanguage(this)
    }

    fun initLanguage(context: Context) {
        val appLanguage = deviceService().getAppLanguage()
        if (appLanguage == Language.FOLLOW_SYSTEM) {
            return
        }

        context.resources?.configuration?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleList.setDefault(LocaleList(appLanguage.locale))
                setLocales(LocaleList(appLanguage.locale))
            } else {
                setLocale(appLanguage.locale)
            }
            context.resources.updateConfiguration(this, context.resources.displayMetrics)
        }
    }

    private fun initSDK() {
        //高德地图隐私合规
        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this, true)
        locationService().init(this)
    }

    private fun registerService() {
        ServiceManager.instance().registerService(ServiceType.HTTP, OkhttpService())
        ServiceManager.instance()
            .registerService(ServiceType.STORAGE, DefaultStorageService(this))
        ServiceManager.instance().registerService(ServiceType.DEVICE, DefaultDeviceService(this))
        ServiceManager.instance().registerService(ServiceType.ACCOUNT, DefaultAccountService())
        ServiceManager.instance().registerService(ServiceType.LOCATION, AmapLocationService())
    }

    override fun apiService(): IHttpService {
        return ServiceManager.instance().getService(ServiceType.HTTP) as IHttpService
    }

    override fun storageService(): IStorageService {
        return ServiceManager.instance().getService(ServiceType.STORAGE) as DefaultStorageService
    }

    override fun deviceService(): IDeviceService {
        return ServiceManager.instance().getService(ServiceType.DEVICE) as IDeviceService
    }

    override fun accountService(): IAccountService {
        return ServiceManager.instance().getService(ServiceType.ACCOUNT) as IAccountService
    }

    override fun locationService(): ILocationService {
        return ServiceManager.instance().getService(ServiceType.LOCATION) as ILocationService
    }

    /**
     * 是否同意隐私政策
     */
    fun isAgree(): Boolean {
        return storageService().getBoolean(UserAgreementDialog.KEY_IS_AGREE_AGREEMENT, false)
    }

}