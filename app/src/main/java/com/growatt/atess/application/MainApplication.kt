package com.growatt.atess.application

import android.os.Process
import com.growatt.atess.service.account.DefaultAccountService
import com.growatt.atess.service.http.OkhttpService
import com.growatt.atess.ui.launch.fragment.UserAgreementDialog
import com.growatt.atess.ui.launch.monitor.UserAgreementMonitor
import com.growatt.lib.LibApplication
import com.growatt.lib.service.ServiceManager
import com.growatt.lib.service.ServiceType
import com.growatt.lib.service.account.IAccountService
import com.growatt.lib.service.device.DefaultDeviceService
import com.growatt.lib.service.device.IDeviceService
import com.growatt.lib.service.http.IHttpService
import com.growatt.lib.service.storage.DefaultStorageService
import com.growatt.lib.service.storage.IStorageService
import com.growatt.lib.util.Util

class MainApplication : LibApplication() {

    companion object {
        private lateinit var instance: MainApplication
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
        if (!isAgree()) {
            UserAgreementMonitor.watch { isAgree, monitor ->
                if (isAgree) {
                    // TODO: 2022/6/24 进行初始化，类似第三方库隐私政策相关的初始化
                }
                monitor.unWatch()
            }
        }
    }

    private fun init() {
        //过滤掉其它进程
        if (!packageName.equals(Util.getProcessNameByPID(this, Process.myPid()))) {
            return
        }
        registerService()
        Foreground.init(this)
    }

    private fun registerService() {
        ServiceManager.instance().registerService(ServiceType.HTTP, OkhttpService())
        ServiceManager.instance()
            .registerService(ServiceType.STORAGE, DefaultStorageService(this))
        ServiceManager.instance().registerService(ServiceType.DEVICE, DefaultDeviceService(this))
        ServiceManager.instance().registerService(ServiceType.ACCOUNT, DefaultAccountService())
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

    /**
     * 是否同意隐私政策
     */
    fun isAgree(): Boolean {
        return storageService().getBoolean(UserAgreementDialog.KEY_IS_AGREE_AGREEMENT, false)
    }

}