package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityHpsInfoBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.HpsModel
import com.growatt.atess.model.plant.ui.IDeviceInfoHead
import com.growatt.atess.ui.plant.fragment.device.DeviceChartFragment
import com.growatt.atess.ui.plant.fragment.device.DeviceHead1Fragment
import com.growatt.atess.ui.plant.fragment.info.HpsSystemOperationFragment
import com.growatt.atess.ui.plant.viewmodel.HpsViewModel
import com.growatt.lib.util.ToastUtil

/**
 * HPS设备详情
 */
class HpsInfoActivity : BaseActivity(), IBaseDeviceActivity, View.OnClickListener {

    companion object {

        private const val KEY_SN = "key_sn"

        fun start(context: Context?, deviceSN: String?) {
            context?.startActivity(Intent(context, HpsInfoActivity::class.java).also {
                it.putExtra(KEY_SN, deviceSN)
            })
        }

    }

    private lateinit var binding: ActivityHpsInfoBinding
    private val viewModel: HpsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHpsInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
    }

    private fun initData() {
        viewModel.deviceSn = intent.getStringExtra(KEY_SN)
        viewModel.getDeviceInfoLiveData.observe(this) {
            if (it.second == null) {
                (supportFragmentManager.findFragmentById(R.id.fragment_head) as DeviceHead1Fragment).bindData(
                    it.first as IDeviceInfoHead
                )
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getDeviceInfo(getDeviceType())
    }

    private fun initView() {
        supportFragmentManager.commit(true) {
            add(
                R.id.fragment_chart,
                DeviceChartFragment(getDeviceType(), HpsModel.createChartType(), viewModel)
            )
        }
        supportFragmentManager.commit(true) {
            replace(
                R.id.fragment_system_operation,
                HpsSystemOperationFragment(null, viewModel.deviceSn)
            )
        }
    }

    override fun onClick(v: View?) {

    }

    override fun getDeviceType(): Int {
        return DeviceType.HPS
    }
}