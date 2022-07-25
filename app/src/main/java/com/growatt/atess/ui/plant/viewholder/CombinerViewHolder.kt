package com.growatt.atess.ui.plant.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growatt.atess.base.OnItemClickListener
import com.growatt.atess.databinding.CombinerViewHolderBinding
import com.growatt.atess.model.plant.DeviceModel

/**
 * Combiner设备(汇流箱设备)
 */
class CombinerViewHolder(
    itemView: View,
    onItemClickListener: OnItemClickListener
) : BaseDeviceViewHolder(itemView, onItemClickListener) {

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClickListener: OnItemClickListener
        ): CombinerViewHolder {
            val binding = CombinerViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = CombinerViewHolder(binding.root, onItemClickListener)
            holder.binding = binding
            binding.root.setOnClickListener(holder)
            return holder
        }
    }

    private lateinit var binding: CombinerViewHolderBinding

    override fun bindData(deviceModel: DeviceModel) {
        binding.tvDeviceModel.text = deviceModel.deviceModel
        binding.tvDeviceSn.text = deviceModel.getDeviceSN()
        binding.tvPower.text = deviceModel.getPowerText()
        binding.tvElectricity.text = deviceModel.getElectricityText()
        binding.tvVoltage.text = deviceModel.getVoltageText()
    }
}