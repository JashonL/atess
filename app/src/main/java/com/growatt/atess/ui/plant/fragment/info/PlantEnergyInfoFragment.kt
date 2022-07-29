package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.activityViewModels
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantEnergyInfoBinding
import com.growatt.atess.databinding.ItemEnergyInfoBinding
import com.growatt.atess.model.plant.DeviceEnergyInfoModel
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.Util

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

    private fun showEnergyInfo(deviceEnergyInfoList: Array<DeviceEnergyInfoModel>?) {
        binding.llContainer.removeAllViews()
        deviceEnergyInfoList?.all {
            if (!TextUtils.isEmpty(it.getTypeName())) {
                binding.llContainer.addView(
                    generateItemView(
                        it.getTypeDrawableResId(),
                        it.getTypeName(),
                        Pair(Util.getDoubleText(it.today), Util.getDoubleText(it.total))
                    )
                )
            }
            true
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