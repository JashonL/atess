package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.databinding.ActivityPbdInfoBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.PbdModel
import com.growatt.atess.ui.plant.fragment.device.DeviceChartFragment
import com.growatt.atess.ui.plant.fragment.device.DeviceHead1Fragment
import com.growatt.atess.ui.plant.viewmodel.DeviceInfoViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.ViewUtil

/**
 * PBD设备详情
 */
class PbdInfoActivity : BaseDeviceActivity(), View.OnClickListener {

    companion object {

        private const val KEY_SN = "key_sn"

        fun start(context: Context?, deviceSN: String?) {
            context?.startActivity(Intent(context, PbdInfoActivity::class.java).also {
                it.putExtra(KEY_SN, deviceSN)
            })
        }

    }

    private lateinit var binding: ActivityPbdInfoBinding
    private val viewModel: DeviceInfoViewModel<PbdModel> by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPbdInfoBinding.inflate(layoutInflater)
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
        viewModel.getDeviceInfo(getDeviceType())
    }

    private fun refreshView(pbdModel: PbdModel) {
        (supportFragmentManager.findFragmentById(R.id.fragment_head) as DeviceHead1Fragment).bindData(
            pbdModel
        )
        binding.tvDeviceStatus.text =
            getString(if (pbdModel.lost == true) R.string.offline else R.string.online)
        binding.tvDeviceStatus.background = if (pbdModel.lost == true) ViewUtil.createShape(
            resources.getColor(R.color.color_0D000000),
            2
        ) else ViewUtil.createShape(resources.getColor(R.color.color_1A3FAE29), 2)
        binding.tvDeviceStatus.setTextColor(resources.getColor(if (pbdModel.lost == true) R.color.text_green else R.color.text_gray_bb))
        binding.tvPower.text = pbdModel.getTotalPowerText()
        binding.tvTodayPower.text = pbdModel.getETodayText()
        binding.tvTotalPower.text = pbdModel.getETotalText()
    }

    private fun initView() {
        binding.tvTodayUnit.text = getString(
            R.string.slash_format,
            getString(R.string.today_power),
            getString(R.string.kwh)
        )
        binding.tvTotalUnit.text = getString(
            R.string.slash_format,
            getString(R.string.total_power),
            getString(R.string.kwh)
        )
        supportFragmentManager.commit(true) {
            add(
                R.id.fragment_chart,
                DeviceChartFragment(getDeviceType(), PbdModel.createChartType())
            )
        }
    }

    override fun onClick(v: View?) {

    }

    override fun getDeviceType(): Int {
        return DeviceType.PBD
    }
}