package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityAddCollectorBinding
import com.growatt.atess.ui.home.HomeActivity
import com.growatt.atess.ui.home.view.HomeTab
import com.growatt.atess.ui.plant.viewmodel.AddCollectorViewModel
import com.growatt.lib.util.ToastUtil

/**
 * 添加采集器页面
 */
class AddCollectorActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_PLANT_ID = "key_plant_id"

        fun start(context: Context?, plantId: String?) {
            context?.startActivity(Intent(context, AddCollectorActivity::class.java).also {
                it.putExtra(KEY_PLANT_ID, plantId)
            })
        }

    }

    private lateinit var binding: ActivityAddCollectorBinding
    private val viewModel: AddCollectorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCollectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
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
    }

    private fun setListener() {
        binding.ivScan.setOnClickListener(this)
        binding.btFinish.setOnClickListener(this)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivScan -> {}
            v === binding.btFinish -> {
                val collectorSN = binding.etCollectorSn.text.trim().toString()
                val checkCode = binding.etCheckCode.text.trim().toString()
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
}