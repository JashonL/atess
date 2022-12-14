package com.growatt.atess.ui.plant.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.R
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.MbmsViewHolderBinding
import com.growatt.atess.model.plant.DeviceModel

/**
 * MBMS设备
 */
class MBmsViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseDeviceViewHolder(itemView, onItemClickListener) {

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): MBmsViewHolder {
            val binding = MbmsViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = MBmsViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: MbmsViewHolderBinding

    override fun bindData(deviceModel: DeviceModel) {
        binding.tvDeviceModel.text = deviceModel.deviceModel
        binding.tvDeviceSn.text = deviceModel.getDeviceSN()
        binding.tvStatus.text = deviceModel.getSysStatusText()
        binding.tvStatus.setTextColor(getColor(if (deviceModel.sysStatus == -1) R.color.color_82DCDC else R.color.color_D4EC59))
        binding.tvSoc.text = deviceModel.getSocText()
        binding.tvConnectStatus.text = deviceModel.getConnectStatusText()
    }
}