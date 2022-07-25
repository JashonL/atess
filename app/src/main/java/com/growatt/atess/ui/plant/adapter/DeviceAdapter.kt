package com.growatt.atess.ui.plant.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.model.plant.DeviceModel
import com.growatt.atess.ui.plant.activity.BaseDeviceActivity
import com.growatt.atess.ui.plant.viewholder.BaseDeviceViewHolder

/**
 * 我的设备列表
 */
class DeviceAdapter : RecyclerView.Adapter<BaseDeviceViewHolder>(), OnItemClickListener {

    init {
        setHasStableIds(true)
    }

    private val deviceList = mutableListOf<DeviceModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDeviceViewHolder {
        return BaseDeviceViewHolder.createDeviceViewHolder(viewType, parent, this)
    }

    override fun onBindViewHolder(holder: BaseDeviceViewHolder, position: Int) {
        holder.bindData(deviceList[position])
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun getItemViewType(position: Int): Int {
        return deviceList[position].getRealDeviceType()
    }

    override fun onItemClick(v: View?, position: Int) {
        BaseDeviceActivity.jumpToDeviceInfoPage(
            v?.context,
            getItemViewType(position),
            deviceList[position].getDeviceSN()
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(deviceList: List<DeviceModel>?) {
        this.deviceList.clear()
        if (!deviceList.isNullOrEmpty()) {
            this.deviceList.addAll(deviceList)
        }
        notifyDataSetChanged()
    }
}