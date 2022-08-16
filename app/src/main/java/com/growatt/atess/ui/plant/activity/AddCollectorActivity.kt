package com.growatt.atess.ui.plant.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityAddCollectorBinding
import com.growatt.atess.ui.common.activity.ScanActivity
import com.growatt.atess.ui.common.fragment.RequestPermissionHub
import com.growatt.atess.ui.home.HomeActivity
import com.growatt.atess.ui.home.view.HomeTab
import com.growatt.atess.ui.plant.viewmodel.AddCollectorViewModel
import com.growatt.lib.util.ActivityBridge
import com.growatt.lib.util.ToastUtil

/**
 * 添加采集器页面
 */
class AddCollectorActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_PLANT_ID = "key_plant_id"

        fun start(context: Context?, plantId: String?) {
            if (MainApplication.instance().accountService().isGuest()) {
                ToastUtil.show(
                    MainApplication.instance().getString(R.string.info_space_not_permission)
                )
            } else {
                context?.startActivity(getIntent(context, plantId))
            }
        }

        fun getIntent(context: Context?, plantId: String?): Intent {
            return Intent(context, AddCollectorActivity::class.java).also {
                it.putExtra(KEY_PLANT_ID, plantId)
            }
        }

    }

    private lateinit var binding: ActivityAddCollectorBinding
    private val viewModel: AddCollectorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCollectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        setListener()
    }

    private fun initData() {
        viewModel.plantId = intent.getStringExtra(KEY_PLANT_ID)
        viewModel.addCollectorLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                HomeActivity.start(this, HomeTab.PLANT)
                finish()
            } else {
                ToastUtil.show(it)
            }
        }
        viewModel.getCheckCodeLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                binding.etCheckCode.setText(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun setListener() {
        binding.ivScan.setOnClickListener(this)
        binding.btFinish.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivScan -> {
                RequestPermissionHub.requestPermission(
                    supportFragmentManager,
                    arrayOf(Manifest.permission.CAMERA)
                ) {
                    if (it) {
                        scan()
                    }
                }
            }
            v === binding.btFinish -> {
                val collectorSN = binding.etCollectorSn.text.toString().trim()
                val checkCode = binding.etCheckCode.text.toString().trim()
                when {
                    collectorSN.isEmpty() -> {
                        ToastUtil.show(getString(R.string.serial_number_not_null))
                    }
                    checkCode.isEmpty() -> {
                        ToastUtil.show(getString(R.string.check_code_not_null))
                    }
                    else -> {
                        showDialog()
                        viewModel.addCollector(collectorSN, checkCode)
                    }
                }
            }
        }
    }

    private fun scan() {
        ActivityBridge.startActivity(
            this,
            ScanActivity.getIntent(this),
            object : ActivityBridge.OnActivityForResult {
                override fun onActivityForResult(
                    context: Context?,
                    resultCode: Int,
                    data: Intent?
                ) {
                    if (resultCode == RESULT_OK && data?.hasExtra(ScanActivity.KEY_CODE_TEXT) == true) {
                        val collectionSN = data.getStringExtra(ScanActivity.KEY_CODE_TEXT)
                        binding.etCollectorSn.setText(collectionSN)
                        showDialog()
                        viewModel.getCheckCode(collectionSN!!)
                    }
                }
            })
    }
}