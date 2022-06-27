package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.growatt.atess.R
import com.growatt.atess.databinding.ActivityLoginBinding
import com.growatt.lib.base.BaseActivity
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.setViewHeight

/**
 * 登录页面
 */
class LoginActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private lateinit var binding: ActivityLoginBinding

    //是否显示密码
    private var isShowPassword = false
    private var focusOnUsername = false
    private var focusOnPassword = false
    private var isAgree = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListener()
    }

    private fun setListener() {
        binding.ivEye.setOnClickListener(this)
        binding.etUserName.setOnFocusChangeListener { v, hasFocus ->
            focusOnUsername = hasFocus
            updateFocusView()
        }
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            focusOnPassword = hasFocus
            updateFocusView()
        }
        binding.ivClear.setOnClickListener(this)
        binding.ivSelect.setOnClickListener(this)
        binding.tvForgetPassword.setOnClickListener(this)
        binding.tvInfoSpace.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
    }

    private fun initView() {
        updatePasswordView(false)
        updateFocusView()
        updateSelectView(false)
        binding.tvUserAgreement.run {
            highlightColor = resources.getColor(android.R.color.transparent)
            movementMethod = LinkMovementMethod.getInstance()
            text = getTvSpan()
        }
    }

    private fun getTvSpan(): SpannableString {
        val userAgreement = getString(R.string.user_agreement)
        val privacyPolicy = getString(R.string.privacy_policy)
        val content = getString(R.string.agree_company_user_agreement, userAgreement, privacyPolicy)
        return SpannableString(content).apply {
            addColorSpan(this, userAgreement)
            addClickSpan(this, userAgreement) {
                ToastUtil.show("点击了用户协议")
            }
            addColorSpan(this, privacyPolicy)
            addClickSpan(this, privacyPolicy) {
                ToastUtil.show("点击了隐私条款")
            }
        }
    }

    private fun addColorSpan(spannable: SpannableString, colorSpanContent: String) {
        val span = ForegroundColorSpan(resources.getColor(R.color.colorAccent))
        val startPosition = spannable.toString().indexOf(colorSpanContent)
        val endPosition = startPosition + colorSpanContent.length
        spannable.setSpan(span, startPosition, endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    private fun addClickSpan(
        spannable: SpannableString,
        clickSpanContent: String,
        click: (View) -> Unit
    ) {
        val span = object : ClickableSpan() {
            override fun onClick(view: View) {
                click(view)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //去掉下划线
                ds.isUnderlineText = false
            }
        }
        val startPosition = spannable.toString().indexOf(clickSpanContent)
        val endPosition = startPosition + clickSpanContent.length
        spannable.setSpan(span, startPosition, endPosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    private fun updateSelectView(isAgree: Boolean) {
        binding.ivSelect.setImageResource(if (isAgree) R.drawable.ic_select else R.drawable.ic_unselect)
        this.isAgree = isAgree
    }

    private fun updatePasswordView(isShowPassword: Boolean) {
        if (isShowPassword) {
            binding.ivEye.setImageResource(R.drawable.ic_eye_open)
            binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            binding.ivEye.setImageResource(R.drawable.ic_eye_close)
            binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        this.isShowPassword = isShowPassword
        binding.etPassword.setSelection(binding.etPassword.length())
    }

    private fun updateFocusView() {
        if (focusOnUsername) {
            binding.etUserName.setSelection(binding.etUserName.length())
            binding.viewUsernameLine.setBackgroundResource(R.color.colorAccent)
            binding.viewUsernameLine.setViewHeight(2f)
        } else {
            binding.viewUsernameLine.setBackgroundResource(R.color.color_line)
            binding.viewUsernameLine.setViewHeight(1f)
        }

        if (focusOnPassword) {
            binding.etPassword.setSelection(binding.etPassword.length())
            binding.viewPasswordLine.setBackgroundResource(R.color.colorAccent)
            binding.viewPasswordLine.setViewHeight(2f)
        } else {
            binding.viewPasswordLine.setBackgroundResource(R.color.color_line)
            binding.viewPasswordLine.setViewHeight(1f)
        }

    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivEye -> {
                updatePasswordView(!isShowPassword)
            }
            v === binding.ivClear -> {
                binding.etUserName.setText("")
            }
            v === binding.ivSelect -> {
                updateSelectView(!isAgree)
            }
            v === binding.tvInfoSpace -> {

            }
            v === binding.tvForgetPassword -> {

            }
            v === binding.tvRegister -> {

            }
        }
    }

}