package com.growatt.atess.ui.plant.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.R
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.PbdViewHolderBinding
import com.growatt.atess.model.plant.DeviceModel

/**
 * PBD设备
 */
class PbdViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseDeviceViewHolder(itemView, onItemClickListener) {

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): PbdViewHolder {
            val binding = PbdViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = PbdViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: PbdViewHolderBinding

    override fun bindData(deviceModel: DeviceModel) {
        binding.tvDeviceModel.text = deviceModel.deviceModel
        binding.tvDeviceSn.text = deviceModel.pbdid
        binding.tvStatus.text = deviceModel.getStatusText()
        binding.tvStatus.setBackgroundResource(if (deviceModel.lost) R.color.text_green else R.color.text_gray_99)
        binding.tvTodayPower.text = deviceModel.getETodayText()
        binding.tvTotalPower.text = deviceModel.getETotalText()
    }
}