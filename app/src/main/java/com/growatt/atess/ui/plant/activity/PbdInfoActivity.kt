package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityPbdInfoBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.PbdModel
import com.growatt.atess.ui.plant.fragment.device.DeviceChartFragment
import com.growatt.atess.ui.plant.fragment.device.DeviceHead1Fragment
import com.growatt.atess.ui.plant.viewmodel.PbdViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.ViewUtil

/**
 * PBD设备详情
 */
class PbdInfoActivity : BaseActivity(), IBaseDeviceActivity, View.OnClickListener {

    companion object {

        private const val KEY_SN = "key_sn"

        fun start(context: Context?, deviceSN: String?) {
            context?.startActivity(Intent(context, PbdInfoActivity::class.java).also {
                it.putExtra(KEY_SN, deviceSN)
            })
        }

    }

    private lateinit var binding: ActivityPbdInfoBinding
    private val viewModel: PbdViewModel by viewModels()

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
        val eTodayWithUnitText = pbdModel.getETodayWithUnitText()
        binding.tvTodayPower.text = eTodayWithUnitText.first
        binding.tvTodayUnit.text = getString(
            R.string.slash_format,
            getString(R.string.today_generate_electricity),
            eTodayWithUnitText.second
        )
        val eTotalWithUnitText = pbdModel.getETotalWithUnitText()
        binding.tvTotalPower.text = eTotalWithUnitText.first
        binding.tvTotalUnit.text = getString(
            R.string.slash_format,
            getString(R.string.total_generate_electricity),
            eTotalWithUnitText.second
        )
    }

    private fun initView() {
        supportFragmentManager.commit(true) {
            add(
                R.id.fragment_chart,
                DeviceChartFragment(getDeviceType(), PbdModel.createChartType(), viewModel)
            )
        }
    }

    override fun onClick(v: View?) {

    }

    override fun getDeviceType(): Int {
        return DeviceType.PBD
    }
}