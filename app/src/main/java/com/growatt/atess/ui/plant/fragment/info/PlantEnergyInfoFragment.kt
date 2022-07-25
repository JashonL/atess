package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.activityViewModels
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantEnergyInfoBinding
import com.growatt.atess.databinding.ItemEnergyInfoBinding
import com.growatt.atess.model.plant.DeviceEnergyInfoModel
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.lib.util.ToastUtil

/**
 * 电站详情-能源概况
 */
class PlantEnergyInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentPlantEnergyInfoBinding

    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantEnergyInfoBinding.inflate(inflater, container, false)
        initData()
        return binding.root
    }

    private fun initData() {
        viewModel.getDeviceEnergyInfoLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                showEnergyInfo(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun showEnergyInfo(deviceEnergyInfoModel: DeviceEnergyInfoModel?) {
        binding.llContainer.removeAllViews()
        deviceEnergyInfoModel?.also {
            if (it.hasPhotovoltaicOut()) {
                binding.llContainer.addView(
                    generateItemView(
                        R.drawable.ic_photovoltaic_output,
                        getString(R.string.photovoltaic_output),
                        deviceEnergyInfoModel.getFormatValues(it.pvout)
                    )
                )
            }
            binding.llContainer.addView(
                generateItemView(
                    R.drawable.ic_diesel_engine_machine_output,
                    getString(R.string.diesel_engine_machine_output),
                    deviceEnergyInfoModel.getFormatValues(it.oilout)
                )
            )
            binding.llContainer.addView(
                generateItemView(
                    R.drawable.ic_battery_charge,
                    getString(R.string.battery_charge),
                    deviceEnergyInfoModel.getFormatValues(it.batCharge)
                )
            )
            binding.llContainer.addView(
                generateItemView(
                    R.drawable.ic_battery_discharge,
                    getString(R.string.battery_discharge),
                    deviceEnergyInfoModel.getFormatValues(it.batDisCharge)
                )
            )
            binding.llContainer.addView(
                generateItemView(
                    R.drawable.ic_load_consumption,
                    getString(R.string.load_consumption),
                    deviceEnergyInfoModel.getFormatValues(it.load)
                )
            )
            binding.llContainer.addView(
                generateItemView(
                    R.drawable.ic_feed_into_the_grid,
                    getString(R.string.feed_into_the_grid),
                    deviceEnergyInfoModel.getFormatValues(it.toGrid)
                )
            )
            binding.llContainer.addView(
                generateItemView(
                    R.drawable.ic_take_electricity_grid,
                    getString(R.string.take_electricity_grid),
                    deviceEnergyInfoModel.getFormatValues(it.fromGrid)
                )
            )
            binding.llContainer.addView(
                generateItemView(
                    R.drawable.ic_grid_connected_inverter_output,
                    getString(R.string.grid_connected_inverter_output),
                    deviceEnergyInfoModel.getFormatValues(it.inverterOut)
                )
            )
        }

    }

    private fun generateItemView(
        @DrawableRes drawableResId: Int,
        name: String,
        dayAndTotal: Pair<String, String>
    ): View {
        val itemBinding = ItemEnergyInfoBinding.inflate(layoutInflater, binding.llContainer, false)
        itemBinding.ivIcon.setImageResource(drawableResId)
        itemBinding.tvName.text = name
        itemBinding.tvPhotovoltaicOutputDay.text = dayAndTotal.first
        itemBinding.tvPhotovoltaicOutputTotal.text = dayAndTotal.second
        return itemBinding.root
    }
}