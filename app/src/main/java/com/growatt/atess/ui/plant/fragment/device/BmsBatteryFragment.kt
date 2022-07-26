package com.growatt.atess.ui.plant.fragment.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentBmsBatteryBinding
import com.growatt.atess.model.plant.BatteryModel
import com.growatt.atess.model.plant.BmsModel
import com.growatt.atess.ui.plant.viewmodel.DeviceInfoViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.invisible
import com.growatt.lib.util.visible

/**
 * 设备详情-BMS、MBMS、BCU_BMS电池信息
 */
class BmsBatteryFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentBmsBatteryBinding

    private val viewModel: DeviceInfoViewModel<BmsModel> by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBmsBatteryBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun setListener() {
        binding.tvSeeMore.setOnClickListener(this)
    }

    private fun initData() {
        viewModel.getDeviceInfoLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                showData(it.first?.batteryVO)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun showData(batteryModel: BatteryModel?) {
        if (batteryModel == null || batteryModel.getBatteryListNoMoreThanTwo().isEmpty()) {
            binding.root.gone()
        } else {
            binding.tvBatteryName.text = batteryModel.batName
            val batteryList = batteryModel.getBatteryListNoMoreThanTwo()
            //设置第一个电池
            if (batteryList.size == 1) {
                val battery1 = batteryList[0]
                showBattery1(battery1)
            }

            //设置第二个电池
            if (batteryList.size == 2) {
                val battery2 = batteryList[1]
                showBattery2(battery2)
                binding.llChildBattery2.visible()
            } else {
                binding.llChildBattery2.invisible()
            }

            binding.root.visible()
        }
    }

    private fun showBattery2(battery2: BatteryModel) {
        binding.ivChildBatteryIcon2.setImageResource(battery2.getIcon())
        binding.tvChildBatteryName2.text = battery2.batName
        if (battery2.showMaxVol()) {
            binding.tvChildMaxVol2.visible()
            binding.tvChildMaxVol2.text = battery2.getMaxVolText()
        } else {
            binding.tvChildMaxVol2.gone()
        }
        if (battery2.showMinVol()) {
            binding.tvChildMinVol2.visible()
            binding.tvChildMinVol2.text = battery2.getMinVolText()
        } else {
            binding.tvChildMinVol2.gone()
        }
        if (battery2.showVol()) {
            binding.tvChildVol2.visible()
            binding.tvChildVol2.text = battery2.getVolText()
        } else {
            binding.tvChildVol2.gone()
        }
    }

    private fun showBattery1(battery1: BatteryModel) {
        binding.ivChildBatteryIcon1.setImageResource(battery1.getIcon())
        binding.tvChildBatteryName1.text = battery1.batName
        if (battery1.showMaxVol()) {
            binding.tvChildMaxVol1.visible()
            binding.tvChildMaxVol1.text = battery1.getMaxVolText()
        } else {
            binding.tvChildMaxVol1.gone()
        }
        if (battery1.showMinVol()) {
            binding.tvChildMinVol1.visible()
            binding.tvChildMinVol1.text = battery1.getMinVolText()
        } else {
            binding.tvChildMinVol1.gone()
        }
        if (battery1.showVol()) {
            binding.tvChildVol1.visible()
            binding.tvChildVol1.text = battery1.getVolText()
        } else {
            binding.tvChildVol1.gone()
        }
    }


    private fun initView() {
        binding.root.gone()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvSeeMore -> {}
        }
    }

}