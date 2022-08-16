package com.growatt.atess.ui.plant.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.R
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.CollectorViewHolderBinding
import com.growatt.atess.model.plant.DeviceModel

/**
 * 采集器设备
 */
class CollectorViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseDeviceViewHolder(itemView, onItemClickListener) {

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): CollectorViewHolder {
            val binding = CollectorViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = CollectorViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: CollectorViewHolderBinding

    override fun bindData(deviceModel: DeviceModel) {
        binding.tvDeviceModel.text = deviceModel.deviceModel
        binding.tvDeviceSn.text = deviceModel.getDeviceSN()
        binding.tvStatus.text = deviceModel.getConnectStatusText2()
        binding.tvStatus.setTextColor(getColor(if (deviceModel.lost == true) R.color.text_green else R.color.text_gray_99))
        binding.tvUpdateInterval.text = deviceModel.getUpdateIntervalText()
        binding.tvLastUpdateTime.text = deviceModel.getLastUpdateTimeText()
        binding.tvSignal.text = deviceModel.getSignalText()
    }
}