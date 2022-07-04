package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityModifyInstallerNoBinding
import com.growatt.atess.ui.mine.viewmodel.SettingViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.setViewHeight

/**
 * 设置-修改安装商编号
 */
class ModifyInstallerNoActivity : BaseActivity(), View.OnClickListener {

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, ModifyInstallerNoActivity::class.java)
            context?.startActivity(intent)
        }

    }

    private lateinit var binding: ActivityModifyInstallerNoBinding

    private val settingViewModel: SettingViewModel by viewModels()

    private var focusOnInstallNo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyInstallerNoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        settingViewModel.modifyInstallerNoLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                accountService().user()?.agentCode = getInputInstallerNoText()
                accountService().saveUserInfo(accountService().user())
                finish()
            } else {
                ToastUtil.show(it)
            }
        }
    }

    private fun setListener() {
        binding.ivClear.setOnClickListener(this)
        binding.title.setOnRightButtonClickListener {
            binding.etInstallerNo.text.toString().trim().apply {
                if (!TextUtils.isEmpty(this) && this != accountService().user()?.agentCode) {
                    showDialog()
                    settingViewModel.modifyInstallerNo(this)
                }
            }
        }
        binding.etInstallerNo.setOnFocusChangeListener { v, hasFocus ->
            focusOnInstallNo = hasFocus
            updateFocusView()
        }
    }

    private fun initView() {
        updateFocusView()
        binding.etInstallerNo.setText(accountService().user()?.agentCode ?: "")
    }

    private fun updateFocusView() {
        if (focusOnInstallNo) {
            binding.etInstallerNo.setSelection(binding.etInstallerNo.length())
            binding.viewInstallerNoLine.setBackgroundResource(R.color.colorAccent)
            binding.viewInstallerNoLine.setViewHeight(2f)
        } else {
            binding.viewInstallerNoLine.setBackgroundResource(R.color.color_line)
            binding.viewInstallerNoLine.setViewHeight(1f)
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivClear -> binding.etInstallerNo.setText("")
        }
    }

    private fun getInputInstallerNoText(): String {
        return binding.etInstallerNo.text.toString().trim()
    }
}