package com.growatt.atess.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.growatt.atess.application.MainApplication
import com.growatt.lib.service.ServiceManager
import com.growatt.lib.service.account.IAccountService
import com.growatt.lib.service.device.IDeviceService
import com.growatt.lib.service.http.IHttpService
import com.growatt.lib.service.storage.IStorageService

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ViewHelper,
    ServiceManager.ServiceInterface {

    override fun showDialog() {
        (itemView.context as? BaseActivity)?.showDialog()
    }

    override fun dismissDialog() {
        (itemView.context as? BaseActivity)?.dismissDialog()
    }

    override fun apiService(): IHttpService {
        return MainApplication.instance().apiService()
    }

    override fun storageService(): IStorageService {
        return MainApplication.instance().storageService()
    }

    override fun deviceService(): IDeviceService {
        return MainApplication.instance().deviceService()
    }

    override fun accountService(): IAccountService {
        return MainApplication.instance().accountService()
    }

}

interface OnItemClickListener {
    fun onItemClick(v: View?, position: Int)
}