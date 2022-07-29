package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantTotalBinding
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.plant.activity.AddPlantActivity
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.ViewUtil

/**
 * 电站详情-总计（头部）
 */
class PlantTotalFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentPlantTotalBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantTotalBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getPlantInfoLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                showData(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun showData(plantModel: PlantModel?) {
        plantModel?.also {
            binding.tvCity.text = it.city
            Glide.with(this@PlantTotalFragment).load(plantModel.plantImgName)
                .placeholder(R.drawable.ic_placeholder_3)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), GranularRoundedCorners(
                            ViewUtil.dp2px(requireContext(), 6f).toFloat(),
                            ViewUtil.dp2px(requireContext(), 6f).toFloat(),
                            0F, 0F
                        )
                    )
                )
                .into(binding.ivPlantImage)
            binding.tvTotalComponentPower.text = it.nominalPowerStr
            binding.tvInstallDate.text = it.createDateText
            binding.tvDailyOutput.text = getString(R.string.kwh_format, it.getETodayText())
            binding.tvMonthlyOutput.text = getString(R.string.kwh_format, it.getMonthlyPowerText())
            binding.tvTotalOutput.text = getString(R.string.kwh_format, it.getETotalText())
        }
    }

    private fun setListener() {
        binding.tvCity.setOnClickListener(this)
    }

    private fun initView() {
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvCity -> {
                val plantInfo = viewModel.getPlantInfoLiveData.value?.first
                if (plantInfo != null) {
                    AddPlantActivity.start(requireContext(), plantInfo)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}