package com.growatt.atess.ui.plant.viewholder

import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.BaseViewHolder
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.model.plant.DeviceModel
import com.growatt.atess.model.plant.DeviceType

/**
 * 设备ViewHolder基类
 */
abstract class BaseDeviceViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseViewHolder(itemView, onItemClickListener) {

    companion object {
        fun createDeviceViewHolder(
            @DeviceType deviceType: Int, parent: ViewGroup,
            onItemClickListener: OnItemClickListener,
        ): BaseDeviceViewHolder {
            return when (deviceType) {
                DeviceType.HPS -> HpsViewHolder.create(parent, onItemClickListener)
                DeviceType.PCS -> PcsViewHolder.create(parent, onItemClickListener)
                DeviceType.PBD -> PbdViewHolder.create(parent, onItemClickListener)
                DeviceType.BMS -> BmsViewHolder.create(parent, onItemClickListener)
                DeviceType.MBMS -> MBmsViewHolder.create(parent, onItemClickListener)
                DeviceType.BCU_BMS -> BcuBmsViewHolder.create(parent, onItemClickListener)
                DeviceType.COMBINER -> CombinerViewHolder.create(parent, onItemClickListener)
                else -> CollectorViewHolder.create(parent, onItemClickListener)
            }
        }
    }

    abstract fun bindData(deviceModel: DeviceModel)
}