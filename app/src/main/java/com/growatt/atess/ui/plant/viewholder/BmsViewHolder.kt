package com.growatt.atess.ui.plant.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.R
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.BmsViewHolderBinding
import com.growatt.atess.model.plant.DeviceModel
import com.growatt.atess.model.plant.DeviceType

/**
 * BMS设备
 */
class BmsViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseDeviceViewHolder(itemView, onItemClickListener) {

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): BmsViewHolder {
            val binding = BmsViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = BmsViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: BmsViewHolderBinding

    override fun bindData(deviceModel: DeviceModel) {
        binding.tvDeviceModel.text = deviceModel.deviceModel
        binding.tvDeviceSn.text = deviceModel.getDeviceSN()
        binding.ivIcon.setImageResource(if (deviceModel.getRealDeviceType() == DeviceType.MBMS) R.drawable.ic_device_mbms else R.drawable.ic_device_bms)
        binding.tvStatus.text = deviceModel.getSysStatusText()
        binding.tvStatus.setTextColor(getColor(if (deviceModel.sysStatus == -1) R.color.color_82DCDC else R.color.color_D4EC59))
        binding.tvSoc.text = deviceModel.getSocText()
        binding.tvConnectStatus.text = deviceModel.getConnectStatusText()
    }
}