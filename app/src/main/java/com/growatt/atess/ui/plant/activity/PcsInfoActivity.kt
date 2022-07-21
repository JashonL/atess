package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityPcsInfoBinding
import com.growatt.atess.model.plant.DeviceType
import com.growatt.atess.model.plant.PcsModel
import com.growatt.atess.ui.plant.fragment.device.DeviceHead1Fragment
import com.growatt.atess.ui.plant.viewmodel.DeviceInfoViewModel
import com.growatt.lib.util.ToastUtil

/**
 * PCS设备详情
 */
class PcsInfoActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_SN = "key_sn"

        fun start(context: Context?, deviceSN: String?) {
            context?.startActivity(Intent(context, PcsInfoActivity::class.java).also {
                it.putExtra(KEY_SN, deviceSN)
            })
        }

    }

    private lateinit var binding: ActivityPcsInfoBinding
    private val viewModel: DeviceInfoViewModel<PcsModel> by viewModels()

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
                (supportFragmentManager.findFragmentById(R.id.fragment_head) as DeviceHead1Fragment).bindData(
                    it.first!!
                )
            } else {
                ToastUtil.show(it.second)
            }
        }
        viewModel.getDeviceInfo(DeviceType.PCS)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {

    }
}