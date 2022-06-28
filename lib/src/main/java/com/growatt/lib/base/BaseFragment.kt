package com.growatt.lib.base

import androidx.fragment.app.Fragment
import com.growatt.lib.LibApplication
import com.growatt.lib.service.ServiceManager
import com.growatt.lib.service.account.IAccountService
import com.growatt.lib.service.device.IDeviceService
import com.growatt.lib.service.http.IHttpService
import com.growatt.lib.service.storage.IStorageService

abstract class BaseFragment : Fragment(), ViewHelper, ServiceManager.ServiceInterface {

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

    override fun showDialog() {
        (activity as? BaseActivity)?.showDialog()
    }

    override fun dismissDialog() {
        (activity as? BaseActivity)?.dismissDialog()
    }
}