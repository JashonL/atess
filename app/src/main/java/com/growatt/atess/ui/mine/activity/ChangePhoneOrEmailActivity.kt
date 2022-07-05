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
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityChangePhoneOrEmailBinding
import com.growatt.atess.ui.mine.fragment.RegisterAccountType
import com.growatt.atess.ui.mine.viewmodel.SettingViewModel
import com.growatt.atess.ui.mine.viewmodel.VerifyCodeViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.setViewHeight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 设置-修改手机或者邮箱
 */
class ChangePhoneOrEmailActivity : BaseActivity(), View.OnClickListener {

    companion object {

        const val KEY_REGISTER_ACCOUNT_TYPE = "key_register_account_type"

        fun start(context: Context?, @RegisterAccountType registerAccountType: Int) {
            val intent = Intent(context, ChangePhoneOrEmailActivity::class.java)
            intent.putExtra(KEY_REGISTER_ACCOUNT_TYPE, registerAccountType)
            context?.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityChangePhoneOrEmailBinding

    private val verifyCodeViewModel: VerifyCodeViewModel by viewModels()

    private val settingViewModel: SettingViewModel by viewModels()

    private var registerAccountType: Int = RegisterAccountType.PHONE

    private var focusOnPhoneOrEmail = false

    private var focusOnVerifyCode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePhoneOrEmailBinding.inflate(layoutInflater)
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
                save()
            } else {
                ToastUtil.show(it)
            }
        }

        settingViewModel.changePhoneOrEmailLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                val phoneOrEmail = binding.etPhoneOrEmail.text.toString().trim()
                if (registerAccountType == RegisterAccountType.PHONE) {
                    accountService().user()?.phoneNum = phoneOrEmail
                } else {
                    accountService().user()?.email = phoneOrEmail
                }
                accountService().saveUserInfo(accountService().user())
                finish()
            } else {
                ToastUtil.show(it)
            }
        }

        registerAccountType =
            intent.getIntExtra(KEY_REGISTER_ACCOUNT_TYPE, RegisterAccountType.PHONE)
    }

    private fun setListener() {
        binding.tvSendVerifyCode.setOnClickListener(this)
        binding.title.setOnRightButtonClickListener {
            verifyCode()
        }
        binding.etPhoneOrEmail.setOnFocusChangeListener { v, hasFocus ->
            focusOnPhoneOrEmail = hasFocus
            updateFocusView()
        }
        binding.etVerifyCode.setOnFocusChangeListener { v, hasFocus ->
            focusOnVerifyCode = hasFocus
            updateFocusView()
        }
    }

    private fun save() {
        val phoneOrEmail = binding.etPhoneOrEmail.text.toString().trim()
        val verifyCode = binding.etVerifyCode.text.toString().trim()
        showDialog()
        settingViewModel.changePhoneOrEmail(phoneOrEmail, registerAccountType, verifyCode)
    }

    private fun initView() {
        showPhoneOrEmailView(registerAccountType)
        updateFocusView()
    }

    private fun updateFocusView() {
        if (focusOnPhoneOrEmail) {
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

    private fun showPhoneOrEmailView(@RegisterAccountType registerAccountType: Int) {
        if (registerAccountType == RegisterAccountType.PHONE) {
            binding.etPhoneOrEmail.setHint(R.string.please_input_phone)
            binding.title.setTitleText(getString(R.string.change_phone))
            binding.etPhoneOrEmail.inputType = InputType.TYPE_CLASS_PHONE
        } else {
            binding.etPhoneOrEmail.setHint(R.string.please_input_email)
            binding.title.setTitleText(getString(R.string.change_email))
            binding.etPhoneOrEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvSendVerifyCode -> requestSendVerifyCode()
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
        if (!TextUtils.isEmpty(phoneOrEmail) && !TextUtils.isEmpty(verifyCode) && verifyCode.length == 6) {
            showDialog()
            verifyCodeViewModel.verifyCode(phoneOrEmail, verifyCode)
        }
    }

}