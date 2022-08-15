package com.growatt.atess.ui.plant.fragment.info

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantDeviceInfoBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.atess.view.dialog.OptionsDialog
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 电站详情里面HPS或PCS设备信息
 */
class PlantDeviceInfoFragment : BaseFragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentPlantDeviceInfoBinding
    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantDeviceInfoBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initView() {
        binding.root.gone()
        binding.tvDeviceSn.paint.also {
            it.flags = Paint.UNDERLINE_TEXT_FLAG
            it.isAntiAlias = true
        }
    }

    private fun initData() {
        viewModel.getPcsHpsSNLiveData.observe(viewLifecycleOwner) {
            if (it.third == null) {
                handlePcsHpsSNList(it.first, it.second)
            } else {
                ToastUtil.show(it.third)
            }
        }
    }

    /**
     * @param snList hps与pcs列表
     * @param isDataChange 数据是否有改变
     */
    private fun handlePcsHpsSNList(snList: MutableList<Pair<Int, String>>?, isDataChange: Boolean) {
        if (snList?.isNullOrEmpty() == true) {
            binding.root.gone()
        } else {
            binding.root.visible()
            if (isDataChange) {
                val defaultDevice = snList[0]
                viewModel.typeAndSn = defaultDevice
                refreshSNView(defaultDevice)
            }
            refreshDeviceInfo()
        }
    }

    private fun refreshSNView(typeAndSN: Pair<Int, String>) {
        binding.tvDeviceType.text = MainApplication.instance()
            .getString(if (typeAndSN.first == DeviceType.HPS) R.string.hps else R.string.pcs)
        binding.tvDeviceSn.text = typeAndSN.second
    }

    private fun setListener() {
        binding.tvDeviceSn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvDeviceSn -> selectSN()
        }
    }

    private fun selectSN() {
        val options = mutableListOf<String>()
        val snList = viewModel.getPcsHpsSNLiveData.value?.first
        snList?.all {
            options.add(it.second)
            true
        }
        if (options.size > 0) {
            OptionsDialog.show(childFragmentManager, options.toTypedArray()) {
                val selectDevice = snList!![it]
                refreshSNView(selectDevice)
                viewModel.typeAndSn = selectDevice
                refreshDeviceInfo()
            }
        }
    }

    private fun refreshDeviceInfo() {
        viewModel.getEnergyInfo()
        viewModel.getChartInfo()
        val systemOperationFragment =
            childFragmentManager.findFragmentById(R.id.fragment_system_operation)
        when (viewModel.typeAndSn?.first) {
            DeviceType.HPS -> {
                if (systemOperationFragment is HpsSystemOperationFragment) {
                    systemOperationFragment.refresh()
                } else {
                    childFragmentManager.commit(true) {
                        replace(
                            R.id.fragment_system_operation,
                            HpsSystemOperationFragment(
                                viewModel.plantId,
                                viewModel.typeAndSn?.second
                            )
                        )
                    }
                }
            }
            DeviceType.PCS -> {
                if (systemOperationFragment is PcsSystemOperationFragment) {
                    systemOperationFragment.refresh()
                } else {
                    childFragmentManager.commit(true) {
                        replace(
                            R.id.fragment_system_operation,
                            PcsSystemOperationFragment(
                                viewModel.plantId,
                                viewModel.typeAndSn?.second
                            )
                        )
                    }
                }
            }
        }
    }

}