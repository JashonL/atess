package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityMbmsInfoBinding
import com.growatt.atess.model.plant.BmsModel
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.ui.plant.fragment.device.BmsHeadFragment
import com.growatt.atess.ui.plant.fragment.device.DeviceChartFragment
import com.growatt.atess.ui.plant.viewmodel.BmsViewModel
import com.growatt.lib.util.ToastUtil

/**
 * MBms设备详情
 */
class MBmsInfoActivity : BaseActivity(), IBaseDeviceActivity, View.OnClickListener {

    companion object {

        private const val KEY_SN = "key_sn"

        fun start(context: Context?, deviceSN: String?) {
            context?.startActivity(Intent(context, MBmsInfoActivity::class.java).also {
                it.putExtra(KEY_SN, deviceSN)
            })
        }

    }

    private lateinit var binding: ActivityMbmsInfoBinding
    private val viewModel: BmsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMbmsInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
    }

    private fun initData() {
        viewModel.deviceSn = intent.getStringExtra(KEY_SN)
        viewModel.getDeviceInfoLiveData.observe(this) {
            if (it.second == null) {
                (supportFragmentManager.findFragmentById(R.id.fragment_head) as BmsHeadFragment).bindData(
                    it.first!!
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
                DeviceChartFragment(getDeviceType(), BmsModel.createChartType(), viewModel)
            )
        }
    }

    override fun onClick(v: View?) {

    }

    override fun getDeviceType(): Int {
        return DeviceType.MBMS
    }
}