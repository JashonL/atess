package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityPcsInfoBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.PcsModel
import com.growatt.atess.ui.plant.fragment.device.DeviceChartFragment
import com.growatt.atess.ui.plant.fragment.device.DeviceHead1Fragment
import com.growatt.atess.ui.plant.viewmodel.PcsViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.ViewUtil

/**
 * PCS设备详情
 */
class PcsInfoActivity : BaseActivity(), IBaseDeviceActivity, View.OnClickListener {

    companion object {

        private const val KEY_SN = "key_sn"

        fun start(context: Context?, deviceSN: String?) {
            if (MainApplication.instance().accountService().isGuest()) {
                ToastUtil.show(
                    MainApplication.instance().getString(R.string.info_space_not_permission)
                )
            } else {
                context?.startActivity(Intent(context, PcsInfoActivity::class.java).also {
                    it.putExtra(KEY_SN, deviceSN)
                })
            }
        }

    }

    private lateinit var binding: ActivityPcsInfoBinding
    private val viewModel: PcsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPcsInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
    }

    private fun initData() {
        viewModel.deviceSn = intent.getStringExtra(KEY_SN)
        viewModel.getDeviceInfoLiveData.observe(this) {
            if (it.second == null) {
                refreshView(it.first!!)
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getDeviceInfo(DeviceType.PCS)
    }

    private fun refreshView(pcsModel: PcsModel) {
        (supportFragmentManager.findFragmentById(R.id.fragment_head) as DeviceHead1Fragment).bindData(
            pcsModel
        )
        binding.tvDeviceStatus.text =
            getString(if (pcsModel.lost == true) R.string.offline else R.string.online)
        binding.tvDeviceStatus.background = if (pcsModel.lost == true) ViewUtil.createShape(
            resources.getColor(R.color.color_0D000000),
            2
        ) else ViewUtil.createShape(resources.getColor(R.color.color_1A3FAE29), 2)
        binding.tvDeviceStatus.setTextColor(resources.getColor(if (pcsModel.lost == true) R.color.text_green else R.color.text_gray_bb))
        binding.tvPower.text = pcsModel.getTotalPowerText()
    }

    private fun initView() {
        supportFragmentManager.commit(true) {
            add(
                R.id.fragment_chart,
                DeviceChartFragment(getDeviceType(), PcsModel.createChartType(), viewModel)
            )
        }
    }

    override fun onClick(v: View?) {

    }

    override fun getDeviceType(): Int {
        return DeviceType.PCS
    }
}