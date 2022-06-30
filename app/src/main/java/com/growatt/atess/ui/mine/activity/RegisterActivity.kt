package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.R
import com.growatt.atess.databinding.ActivityRegisterBinding
import com.growatt.atess.ui.mine.fragment.VerifyCodeDialog
import com.growatt.atess.ui.mine.viewmodel.RegisterViewModel
import com.growatt.atess.ui.mine.viewmodel.VerifyCodeViewModel
import com.growatt.lib.base.BaseActivity
import com.growatt.lib.util.ActivityBridge
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.invisible
import com.growatt.lib.util.visible

/**
 * 注册页面
 */
class RegisterActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, RegisterActivity::class.java))
        }
    }

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()
    private val verifyCodeViewModel: VerifyCodeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        verifyCodeViewModel.getVerifyCodeLiveData.observe(this) {
            dismissDialog()
            if (!VerifyCodeDialog.isShowing(supportFragmentManager)) {
                if (it.second == null) {
                    VerifyCodeDialog.showDialog(
                        supportFragmentManager,
                        it.first,
                        viewModel.getRequirePhoneOrEmail(),
                        viewModel.getRegisterAccountType()
                    ) {
                        showDialog()
                        val username = binding.etUsername.text.toString().trim()
                        val password = binding.etPassword.text.toString().trim()
                        val agentCode = binding.etInstallerNo.text.toString().trim()
                        viewModel.register(username, password, agentCode)
                    }
                } else {
                    ToastUtil.show(it.second)
                }
            }
        }

        viewModel.registerLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                //注册成功，关闭页面返回登录页面
                finish()
            } else {
                ToastUtil.show(it)
            }
        }
    }

    private fun setListener() {
        binding.tvSelectArea.setOnClickListener(this)
        binding.ivSelect.setOnClickListener(this)
        binding.btFinish.setOnClickListener(this)
    }

    private fun initView() {
        binding.tvUserAgreement.run {
            highlightColor = resources.getColor(android.R.color.transparent)
            movementMethod = LinkMovementMethod.getInstance()
            text = getTvSpan()
        }
        updateSelectView(false)
        updateRequiredView()
    }

    private fun updateRequiredView() {
        if (viewModel.isChina()) {
            binding.tvRequiredPhone.visible()
            binding.tvRequiredEmail.invisible()
        } else {
            binding.tvRequiredPhone.invisible()
            binding.tvRequiredEmail.visible()
        }
    }

    private fun updateSelectView(isAgree: Boolean) {
        binding.ivSelect.setImageResource(if (isAgree) R.drawable.ic_select else R.drawable.ic_unselect)
        viewModel.isAgree = isAgree
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

    override fun onClick(v: View?) {
        when {
            v === binding.tvSelectArea -> selectArea()
            v === binding.ivSelect -> updateSelectView(!viewModel.isAgree)
            v === binding.btFinish -> {
                checkInputInfo()
            }
        }
    }

    private fun selectArea() {
        ActivityBridge.startActivity(
            this,
            SelectAreaActivity.getIntent(this),
            object : ActivityBridge.OnActivityForResult {
                override fun onActivityForResult(
                    context: Context?,
                    resultCode: Int,
                    data: Intent?
                ) {
                    if (resultCode == RESULT_OK && data?.hasExtra(SelectAreaActivity.KEY_AREA) == true) {
                        viewModel.selectArea =
                            data.getStringExtra(SelectAreaActivity.KEY_AREA) ?: ""
                        binding.tvSelectArea.text = viewModel.selectArea
                        updateRequiredView()
                    }
                }
            })
    }

    /**
     * 检查输入信息是否合规
     */
    private fun checkInputInfo() {

        val username = binding.etUsername.text.trim()
        val password = binding.etPassword.text.trim()
        val confirmPassword = binding.etConfirmPassword.text.trim()
        val phone = binding.etPhone.text.trim()
        val email = binding.etEmail.text.trim()

        if (!viewModel.isAgree) {
            ToastUtil.show(getString(R.string.please_check_agree_agreement))
        } else if (TextUtils.isEmpty(username)) {
            ToastUtil.show(getString(R.string.please_input_username))
        } else if (TextUtils.isEmpty(viewModel.selectArea)) {
            ToastUtil.show(getString(R.string.unselected_country))
        } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            ToastUtil.show(getString(R.string.password_cant_empty))
        } else if (password != confirmPassword) {
            ToastUtil.show(getString(R.string.passwords_are_inconsistent))
        } else if (viewModel.isChina() && TextUtils.isEmpty(phone)) {
            ToastUtil.show(getString(R.string.no_phone_number))
        } else if (!viewModel.isChina() && TextUtils.isEmpty(email)) {
            ToastUtil.show(getString(R.string.no_email))
        } else {
            //校验成功
            viewModel.phone = phone.toString()
            viewModel.email = email.toString()
            showDialog()
            verifyCodeViewModel.fetchVerifyCode(
                viewModel.getRequirePhoneOrEmail(),
                viewModel.getRegisterAccountType()
            )
        }
    }

}