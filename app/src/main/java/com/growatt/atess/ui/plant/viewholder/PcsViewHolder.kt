package com.growatt.atess.ui.plant.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.R
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.PcsViewHolderBinding
import com.growatt.atess.model.plant.DeviceModel

/**
 * PCS设备
 */
class PcsViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseDeviceViewHolder(itemView, onItemClickListener) {

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): PcsViewHolder {
            val binding = PcsViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = PcsViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: PcsViewHolderBinding

    override fun bindData(deviceModel: DeviceModel) {
        binding.tvDeviceModel.text = deviceModel.deviceModel
        binding.tvDeviceSn.text = deviceModel.getDeviceSN()
        binding.tvStatus.text = deviceModel.getStatusText()
        binding.tvStatus.setTextColor(getColor(if (deviceModel.lost == true) R.color.text_gray_99 else R.color.text_green))
        binding.tvTodayPower.text = deviceModel.getETodayText()
        binding.tvTotalPower.text = deviceModel.getETotalText()
    }
}