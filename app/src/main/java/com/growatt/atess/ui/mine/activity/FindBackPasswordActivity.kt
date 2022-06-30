package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.growatt.atess.R
import com.growatt.atess.databinding.ActivityFindBackPasswordBinding
import com.growatt.atess.ui.mine.fragment.RegisterAccountType
import com.growatt.atess.ui.mine.viewmodel.VerifyCodeViewModel
import com.growatt.lib.base.BaseActivity
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.setViewHeight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 找回密码页面
 * 1.国内使用手机号找回密码
 * 2.国外使用邮箱找回密码
 */
class FindBackPasswordActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, FindBackPasswordActivity::class.java))
        }
    }

    private lateinit var binding: ActivityFindBackPasswordBinding

    private val verifyCodeViewModel: VerifyCodeViewModel by viewModels()

    private var registerAccountType: Int = RegisterAccountType.PHONE

    private var focusOnPhone = false
    private var focusOnVerifyCode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindBackPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        verifyCodeViewModel.getVerifyCodeLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                updateCountDown(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }

        verifyCodeViewModel.verifyCodeLiveData.observe(this) {
            dismissDialog()
            if (it == null) {

            } else {
                ToastUtil.show(it)
            }
        }
    }

    private fun setListener() {
        binding.title.setOnRightTextClickListener {
            if (registerAccountType == RegisterAccountType.PHONE) {
                refreshView(RegisterAccountType.EMAIL)
            } else {
                refreshView(RegisterAccountType.PHONE)
            }
        }
        binding.etPhoneOrEmail.setOnFocusChangeListener { v, hasFocus ->
            focusOnPhone = hasFocus
            updateFocusView()
        }
        binding.etVerifyCode.setOnFocusChangeListener { v, hasFocus ->
            focusOnVerifyCode = hasFocus
            updateFocusView()
        }
    }

    private fun initView() {
        refreshView(RegisterAccountType.PHONE)
        updateFocusView()
        binding.tvSendVerifyCode.setOnClickListener(this)
    }

    private fun updateFocusView() {
        if (focusOnPhone) {
            binding.etPhoneOrEmail.setSelection(binding.etPhoneOrEmail.length())
            binding.viewPhoneOrEmailLine.setBackgroundResource(R.color.colorAccent)
            binding.viewPhoneOrEmailLine.setViewHeight(2f)
        } else {
            binding.viewPhoneOrEmailLine.setBackgroundResource(R.color.color_line)
            binding.viewPhoneOrEmailLine.setViewHeight(1f)
        }

        if (focusOnVerifyCode) {
            binding.etVerifyCode.setSelection(binding.etVerifyCode.length())
            binding.viewVerifyCodeLine.setBackgroundResource(R.color.colorAccent)
            binding.viewVerifyCodeLine.setViewHeight(2f)
        } else {
            binding.viewVerifyCodeLine.setBackgroundResource(R.color.color_line)
            binding.viewVerifyCodeLine.setViewHeight(1f)
        }

    }

    private fun refreshView(@RegisterAccountType registerAccountType: Int) {
        this.registerAccountType = registerAccountType
        binding.etPhoneOrEmail.text.clear()
        binding.etVerifyCode.text.clear()
        if (registerAccountType == RegisterAccountType.PHONE) {
            binding.etPhoneOrEmail.setHint(R.string.please_input_phone)
            binding.title.setRightText(getString(R.string.email_find_back))
            binding.etPhoneOrEmail.inputType = InputType.TYPE_CLASS_PHONE
        } else {
            binding.etPhoneOrEmail.setHint(R.string.please_input_email)
            binding.title.setRightText(getString(R.string.sms_find_back))
            binding.etPhoneOrEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvSendVerifyCode -> requestSendVerifyCode()
            v === binding.btNextStep -> verifyCode()
        }
    }

    private fun updateCountDown(remainingTime: Int) {
        lifecycleScope.launch {
            if (remainingTime > 0) {
                binding.tvSendVerifyCode.isEnabled = false
                for (i in remainingTime downTo 0) {
                    if (i == 0) {
                        binding.tvSendVerifyCode.isEnabled = true
                        binding.tvSendVerifyCode.text = getString(R.string.send_verify_code)
                    } else {
                        binding.tvSendVerifyCode.text = getString(R.string.second_after_send, i)
                        delay(1000)
                    }
                }
            } else {
                binding.tvSendVerifyCode.isEnabled = true
                binding.tvSendVerifyCode.text = getString(R.string.send_verify_code)
            }
        }
    }

    private fun requestSendVerifyCode() {
        showDialog()
        val phoneOrEmail = binding.etPhoneOrEmail.text.toString().trim()
        verifyCodeViewModel.fetchVerifyCode(phoneOrEmail, registerAccountType)
    }

    private fun verifyCode() {
        val verifyCode = binding.etVerifyCode.text.toString().trim()
        val phoneOrEmail = binding.etPhoneOrEmail.text.toString().trim()
        if (!TextUtils.isEmpty(verifyCode) && verifyCode.length == 6) {
            showDialog()
            verifyCodeViewModel.verifyCode(phoneOrEmail, verifyCode)
        }
    }

}