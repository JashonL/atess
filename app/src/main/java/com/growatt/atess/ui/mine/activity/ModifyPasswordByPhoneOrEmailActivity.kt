package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.R
import com.growatt.atess.databinding.ActivityModifyPasswordByPhoneOrEmailBinding
import com.growatt.atess.ui.mine.viewmodel.FindBackPasswordViewModel
import com.growatt.lib.base.BaseActivity
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.setViewHeight

/**
 * 找回密码-修改密码页面
 */
class ModifyPasswordByPhoneOrEmailActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_PHONE_OR_EMAIL = "key_phone_or_email"

        fun start(context: Context?, phoneOrEmail: String) {
            val intent = Intent(context, ModifyPasswordByPhoneOrEmailActivity::class.java)
            intent.putExtra(KEY_PHONE_OR_EMAIL, phoneOrEmail)
            context?.startActivity(intent)
        }

    }

    private lateinit var binding: ActivityModifyPasswordByPhoneOrEmailBinding

    //是否显示密码
    private var isShowPassword = false
    private var isShowConfirmPassword = false
    private var focusOnConfirmPassword = false
    private var focusOnPassword = false
    private var phoneOrEmail: String? = null

    private val viewModel: FindBackPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyPasswordByPhoneOrEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        phoneOrEmail = intent.getStringExtra(KEY_PHONE_OR_EMAIL)
        viewModel.modifyPasswordLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                finish()
            }
        }
    }

    private fun setListener() {
        binding.ivPasswordEye.setOnClickListener(this)
        binding.ivConfirmPasswordEye.setOnClickListener(this)
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            focusOnPassword = hasFocus
            updateFocusView()
        }
        binding.etConfirmPassword.setOnFocusChangeListener { v, hasFocus ->
            focusOnConfirmPassword = hasFocus
            updateFocusView()
        }
        binding.title.setOnRightButtonClickListener {
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.trim()
            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                ToastUtil.show(getString(R.string.password_cant_empty))
            } else if (password.length < 6 || confirmPassword.length < 6) {
                ToastUtil.show(getString(R.string.password_cannot_be_less_than_6_digits))
            } else if (password != confirmPassword) {
                ToastUtil.show(getString(R.string.passwords_are_inconsistent))
            } else {
                phoneOrEmail?.let {
                    showDialog()
                    viewModel.modifyPassword(it, password)
                }
            }
        }
    }

    private fun initView() {
        updateFocusView()
        updatePasswordView()
    }

    private fun updateFocusView() {
        if (focusOnPassword) {
            binding.etPassword.setSelection(binding.etPassword.length())
            binding.viewPasswordLine.setBackgroundResource(R.color.colorAccent)
            binding.viewPasswordLine.setViewHeight(2f)
        } else {
            binding.viewPasswordLine.setBackgroundResource(R.color.color_line)
            binding.viewPasswordLine.setViewHeight(1f)
        }

        if (focusOnConfirmPassword) {
            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
            binding.viewConfirmPasswordLine.setBackgroundResource(R.color.colorAccent)
            binding.viewConfirmPasswordLine.setViewHeight(2f)
        } else {
            binding.viewConfirmPasswordLine.setBackgroundResource(R.color.color_line)
            binding.viewConfirmPasswordLine.setViewHeight(1f)
        }
    }

    private fun updatePasswordView() {
        if (isShowPassword) {
            binding.ivPasswordEye.setImageResource(R.drawable.ic_eye_open)
            binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            binding.ivPasswordEye.setImageResource(R.drawable.ic_eye_close)
            binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.etPassword.setSelection(binding.etPassword.length())

        if (isShowConfirmPassword) {
            binding.ivConfirmPasswordEye.setImageResource(R.drawable.ic_eye_open)
            binding.etConfirmPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            binding.ivConfirmPasswordEye.setImageResource(R.drawable.ic_eye_close)
            binding.etConfirmPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivPasswordEye -> {
                isShowPassword = !isShowPassword
                updatePasswordView()
            }
            v === binding.ivConfirmPasswordEye -> {
                isShowConfirmPassword = !isShowConfirmPassword
                updatePasswordView()
            }
        }
    }

}