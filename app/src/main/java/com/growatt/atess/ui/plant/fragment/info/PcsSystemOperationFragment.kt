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
import com.growatt.atess.databinding.FragmentPcsSystemOperationBinding
import com.growatt.atess.model.plant.DirectionType
import com.growatt.atess.model.plant.ui.PcsSystemOperationModel
import com.growatt.atess.ui.plant.activity.PlantDeviceListActivity
import com.growatt.atess.ui.plant.viewmodel.PcsViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.invisible
import com.growatt.lib.util.visible

/**
 * PCS设备系统运行图
 */
class PcsSystemOperationFragment(val plantId: String?, val deviceSn: String?) : BaseFragment(),
    View.OnClickListener {

    private var _binding: FragmentPcsSystemOperationBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PcsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPcsSystemOperationBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initView() {
        binding.root.gone()
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

    private fun setListener() {
        binding.root.setOnClickListener(this)
    }

    private fun showData(pcsSystemOperationModel: PcsSystemOperationModel?) {
        if (pcsSystemOperationModel == null) {
            binding.root.gone()
        } else {
            binding.root.visible()
            pcsSystemOperationModel.also {
                //处理图标的展示
                if (it.isShowPbd()) {
                    binding.ivPbd.visible()
                    binding.tvPbdOutputPower.visible()
                    binding.tvPbdOutputPower.text = it.getPbdText()
                } else {
                    binding.ivPbd.invisible()
                    binding.tvPbdOutputPower.invisible()
                }
                binding.tvBatteryPercent.text = it.getBatteryPercentText()
                binding.tvChargePower.text = it.getChargeText()
                if (it.isShowPV()) {
                    binding.ivPhotovoltaic.visible()
                    binding.tvPvPower.visible()
                    binding.tvPvPower.text = it.getPVText()
                } else {
                    binding.ivPhotovoltaic.invisible()
                    binding.tvPvPower.invisible()
                }
                if (it.isShowBypass()) {
                    binding.ivBypass.setImageResource(R.drawable.ic_operation_bypass)
                    binding.tvBypass.visible()
                } else {
                    binding.ivBypass.setImageResource(R.drawable.ic_operation_add)
                    binding.tvBypass.invisible()
                }
                if (it.isShowOilEngine()) {
                    binding.ivOilEngine.visible()
                    binding.tvOilEnginePower.text = it.getOilEngineText()
                    binding.tvOilEnginePower.visible()
                } else {
                    binding.ivOilEngine.invisible()
                    binding.tvOilEnginePower.invisible()
                }
                binding.tvGridPower.text = it.getGridText()
                binding.tvLoadPower.text = it.getLoadText()
                if (it.isShowInverter()) {
                    binding.ivInverter.visible()
                    binding.tvInverter.visible()
                } else {
                    binding.ivInverter.gone()
                    binding.tvInverter.gone()
                }

                //处理左右方向的流向
                leftRightDirection(binding.lavPbd, it.getPbdDirection())
                leftRightDirection(binding.lavBattery, it.getBatteryDirection())
                leftRightDirection(binding.lavGrid, it.getGridDirection())
                leftRightDirection(binding.lavOperationLeft, it.getLeftDirection())
                //处理上下方向的流向
                topDownDirection(binding.lavPhotovoltaic, it.getPVDirection())
                topDownDirection(binding.lavPcs, it.getPcsDirection())
                topDownDirection(binding.lavBypass, it.getByPassDirection())
                topDownDirection(binding.lavOilEngine, it.getOilEngineDirection())
                topDownDirection(binding.lavLoad, it.getLoadDirection())
                topDownDirection(binding.lavInverter, it.getInverterDirection())
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

    fun refresh() {
        viewModel.getPcsSystemOperationInfo(deviceSn)
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