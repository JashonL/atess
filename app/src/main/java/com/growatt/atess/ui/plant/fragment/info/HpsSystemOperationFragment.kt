package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentHpsSystemOperationBinding
import com.growatt.atess.model.plant.DirectionType
import com.growatt.atess.model.plant.HpsSystemOperationModel
import com.growatt.atess.ui.plant.activity.PlantDeviceListActivity
import com.growatt.atess.ui.plant.viewmodel.HpsViewModel
import com.growatt.lib.util.*

/**
 * HPS设备系统运行图
 */
class HpsSystemOperationFragment(val plantId: String?, val deviceSn: String?) : BaseFragment(),
    View.OnClickListener {

    private var _binding: FragmentHpsSystemOperationBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HpsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHpsSystemOperationBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initView() {
        binding.root.gone()
        binding.tvSystemStatus.background =
            ViewUtil.createShape(resources.getColor(R.color.color_green_0d), 4)
    }

    private fun setListener() {
        binding.root.setOnClickListener(this)
    }

    private fun initData() {
        viewModel.getSystemOperationInfoLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                showData(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
        refresh()
    }

    fun refresh() {
        viewModel.getHpsSystemOperationInfo(deviceSn)
    }

    private fun showData(hpsSystemOperationModel: HpsSystemOperationModel?) {
        if (hpsSystemOperationModel == null) {
            binding.root.gone()
        } else {
            binding.root.visible()
            binding.tvRunMode.text = hpsSystemOperationModel.runModel
            binding.tvSystemStatus.text = hpsSystemOperationModel.status
            hpsSystemOperationModel.also {
                //处理图标的显示隐藏和数值的显示
                if (it.isShowATS()) {
                    binding.ivAts.setImageResource(R.drawable.ic_operation_ats)
                } else {
                    binding.ivAts.setImageResource(R.drawable.ic_operation_down)
                }
                if (it.isShowOilEngine()) {
                    binding.ivOilEngine.visible()
                    binding.tvOilEnginePower.visible()
                    binding.tvOilEnginePower.text = it.getOilEngineText()
                } else {
                    binding.ivOilEngine.invisible()
                    binding.tvOilEnginePower.invisible()
                }
                binding.tvGridPower.text = it.getGridText()
                binding.tvPvPower.text = it.getPVText()
                binding.tvLoadPower.text = it.getLoadText()
                binding.tvChargePower.text = it.getChargeText()
                binding.tvBatteryPercent.text = it.getBatteryPercentText()

                //处理左右方向的流向
                leftRightDirection(binding.lavGrid, it.getGridDirection())
                leftRightDirection(binding.lavOilEngine, it.getOilEngineDirection())
                leftRightDirection(binding.lavPhotovoltaic, it.getPVDirection())
                leftRightDirection(binding.lavLoad, it.getLoadDirection())
                //处理上下方向的流向
                topDownDirection(binding.lavAts, it.getAtsDirection())
                topDownDirection(binding.lavBattery, it.getBatteryDirection())

            }
        }

    }

    private fun leftRightDirection(
        directionView: LottieAnimationView,
        @DirectionType directionType: Int
    ) {
        when (directionType) {
            DirectionType.OUTPUT -> {
                directionView.visible()
                directionView.setAnimation(R.raw.lottie_arrow_left)
            }
            DirectionType.INPUT -> {
                directionView.visible()
                directionView.setAnimation(R.raw.lottie_arrow_right)
            }
            DirectionType.HIDE -> directionView.invisible()
        }
    }

    private fun topDownDirection(
        directionView: LottieAnimationView,
        @DirectionType directionType: Int
    ) {
        when (directionType) {
            DirectionType.OUTPUT -> {
                directionView.visible()
                directionView.setAnimation(R.raw.lottie_arrow_top)
            }
            DirectionType.INPUT -> {
                directionView.visible()
                directionView.setAnimation(R.raw.lottie_arrow_down)
            }
            DirectionType.HIDE -> directionView.invisible()
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.root -> {
                if (!TextUtils.isEmpty(plantId)) PlantDeviceListActivity.start(
                    plantId,
                    requireActivity()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}