package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentHpsSystemOperationBinding
import com.growatt.atess.model.plant.HpsSystemOperationModel
import com.growatt.atess.ui.plant.activity.MyDeviceListActivity
import com.growatt.atess.ui.plant.viewmodel.HpsViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.invisible
import com.growatt.lib.util.visible

/**
 * HPS设备系统运行图
 */
class HpsSystemOperationFragment(val plantId: String?, val deviceSn: String?) : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentHpsSystemOperationBinding

    private val viewModel: HpsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHpsSystemOperationBinding.inflate(inflater, container, false)
        initData()
        setListener()
        return binding.root
    }

    private fun setListener() {
        binding.ivHps.setOnClickListener(this)
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
        hpsSystemOperationModel?.also {
            if (it.isShowATS()) {
                binding.ivAts.setImageResource(R.drawable.ic_operation_ats)
            } else {
                binding.ivAts.setImageResource(R.drawable.ic_operation_down)
            }
            if (it.isShowOilEngine()) {
                binding.ivOilEngine.visible()
                binding.tvOilEnginePower.visible()
                binding.lavOilEngine.visible()
                binding.tvOilEnginePower.text = it.getOilEngineText()
            } else {
                binding.ivOilEngine.invisible()
                binding.tvOilEnginePower.invisible()
                binding.lavOilEngine.invisible()
            }
            binding.tvGridPower.text = it.getGridText()
            binding.tvPvPower.text = it.getPVText()
            binding.tvLoadPower.text = it.getLoadText()
            binding.tvChargePower.text = it.getChargeText()
            binding.tvBatteryPercent.text = it.getBatteryPercentText()

            when (it.gridFlowDirection()) {
                HpsSystemOperationModel.OUTPUT -> binding.lavGrid.setAnimation(R.raw.lottie_arrow_right)
                HpsSystemOperationModel.INPUT -> binding.lavGrid.setAnimation(R.raw.lottie_arrow_left)
            }

            when (it.atsFlowDirection()) {
                HpsSystemOperationModel.OUTPUT -> binding.lavAts.setAnimation(R.raw.lottie_arrow_down)
                HpsSystemOperationModel.INPUT -> binding.lavAts.setAnimation(R.raw.lottie_arrow_top)
            }

            when (it.batteryFlowDirection()) {
                HpsSystemOperationModel.OUTPUT -> binding.lavBattery.setAnimation(R.raw.lottie_arrow_top)
                HpsSystemOperationModel.INPUT -> binding.lavBattery.setAnimation(R.raw.lottie_arrow_down)
            }
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivHps -> if (plantId != null) MyDeviceListActivity.start(
                plantId,
                requireActivity()
            )
        }
    }

}