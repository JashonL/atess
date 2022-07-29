package com.growatt.atess.ui.plant.fragment.device

import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentBmsDeviceBinding
import com.growatt.atess.model.plant.BmsModel
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.ui.plant.activity.IBaseDeviceActivity
import com.growatt.lib.util.Util
import com.growatt.lib.util.ViewUtil

/**
 * 设备详情-BMS、MBMS、BCU_BMS头部
 */
class BmsHeadFragment : BaseFragment() {
    private var _binding: FragmentBmsDeviceBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmsDeviceBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {

    }

    fun bindData(bmsModel: BmsModel) {
        if (requireActivity() is IBaseDeviceActivity) {
            when ((requireActivity() as IBaseDeviceActivity).getDeviceType()) {
                DeviceType.BMS -> binding.ivIcon.setImageResource(R.drawable.ic_device_bms)
                DeviceType.MBMS -> binding.ivIcon.setImageResource(R.drawable.ic_device_mbms)
                DeviceType.BCU_BMS -> binding.ivIcon.setImageResource(R.drawable.ic_device_bms)
            }
        }
        binding.tvSoc.text = bmsModel.getCurrentBatterySocText()
        binding.tvStatus.text = bmsModel.getSysStatusText()
        binding.tvStatus.setBackgroundResource(if (bmsModel.batFlag == -1) R.color.color_82DCDC else R.color.color_D4EC59)

        binding.llContainer.removeAllViews()
        binding.llContainer.addView(
            generateItemView(
                getString(R.string.battery_total_voltage),
                Util.getDoubleText(bmsModel.vBat) + "V"
            )
        )
        binding.llContainer.addView(
            generateItemView(
                getString(R.string.battery_total_electricity),
                Util.getDoubleText(bmsModel.cBat) + "A"
            )
        )
        binding.llContainer.addView(
            generateItemView(
                getString(R.string.max_temperature),
                bmsModel.maxTempStr
            )
        )
        binding.llContainer.addView(
            generateItemView(
                getString(R.string.min_temperature),
                bmsModel.minTempStr
            )
        )
        binding.llContainer.addView(
            generateItemView(
                getString(R.string.monomer_max_voltage),
                bmsModel.maxVoltStr
            )
        )
        binding.llContainer.addView(
            generateItemView(
                getString(R.string.monomer_min_voltage),
                bmsModel.minVoltStr
            )
        )
    }

    private fun generateItemView(key: String, value: String?): View {
        return LinearLayout(requireContext()).also {
            it.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            it.gravity = Gravity.CENTER_VERTICAL
            it.addView(generateText(R.color.text_gray_99, key))
            if (!TextUtils.isEmpty(value)) {
                it.addView(generateText(R.color.text_black, value!!))
            }
        }
    }

    private fun generateText(@ColorRes colorResId: Int, text: String): View {
        return TextView(requireContext()).also {
            it.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_medium))
            it.setTextColor(resources.getColor(colorResId))
            it.text = text
            it.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            it.setPadding(ViewUtil.dp2px(requireContext(), 20f), 3, 0, 3)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}