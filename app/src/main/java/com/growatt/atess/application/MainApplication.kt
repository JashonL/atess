package com.growatt.atess.application

import android.os.Process
import com.growatt.lib.LibApplication
import com.growatt.lib.service.ServiceManager
import com.growatt.lib.service.ServiceType
import com.growatt.lib.service.device.DefaultDeviceService
import com.growatt.lib.service.device.IDeviceService
import com.growatt.lib.service.http.IHttpService
import com.growatt.lib.service.http.okhttp.OkhttpService
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
        ServiceManager.instance().registerService(ServiceType.STORAGE, DefaultStorageService(this))
        ServiceManager.instance().registerService(ServiceType.DEVICE, DefaultDeviceService(this))
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

}