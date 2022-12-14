package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.BuildConfig
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityLoginBinding
import com.growatt.atess.ui.common.activity.WebActivity
import com.growatt.atess.ui.common.fragment.DebugDialog
import com.growatt.atess.ui.home.HomeActivity
import com.growatt.atess.ui.mine.viewmodel.LoginViewModel
import com.growatt.lib.service.account.BaseAccountService
import com.growatt.lib.service.account.User
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

        fun startClearTask(context: Context?) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context?.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityLoginBinding

    //是否显示密码
    private var isShowPassword = false
    private var focusOnUsername = false
    private var focusOnPassword = false
    private var isAgree = false

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        viewModel.loginLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                val user = it.first
                loginSuccess(user)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun loginSuccess(user: User?) {
        accountService().saveToken(user?.token)
        accountService().saveUserInfo(user)
        HomeActivity.start(this)
        finish()
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
        binding.tvFindBackPassword.setOnClickListener(this)
        binding.tvInfoSpace.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)
        binding.btLogin.setOnClickListener(this)
        binding.ivLogo.setOnClickListener(this)
        binding.tvAppName.setOnClickListener(this)
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
                WebActivity.start(this@LoginActivity, BuildConfig.userAgreementUrl)
            }
            addColorSpan(this, privacyPolicy)
            addClickSpan(this, privacyPolicy) {
                WebActivity.start(this@LoginActivity, BuildConfig.privacyPolicyUrl)
            }
        }
    }

    private fun addColorSpan(spannable: SpannableString, colorSpanContent: String) {
        val span = ForegroundColorSpan(resources.getColor(R.color.text_red))
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
                if (!isAgree) {
                    ToastUtil.show(getString(R.string.please_check_agree_agreement))
                } else {
                    showDialog()
                    viewModel.login(
                        BaseAccountService.INFO_SPACE_USER_NAME,
                        BaseAccountService.INFO_SPACE_PASSWORD
                    )
                }
            }
            v === binding.tvFindBackPassword -> {
                FindBackPasswordActivity.start(this)
            }
            v === binding.tvRegister -> {
                RegisterActivity.start(this)
            }
            v === binding.btLogin -> {
                checkInfo()
            }
            v === binding.ivLogo -> clickDebug(binding.ivLogo)
            v === binding.tvAppName -> clickDebug(binding.tvAppName)
        }
    }

    private fun checkInfo() {
        val userName = binding.etUserName.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        when {
            TextUtils.isEmpty(userName) -> {
                ToastUtil.show(getString(R.string.please_input_username))
            }
            TextUtils.isEmpty(password) -> {
                ToastUtil.show(getString(R.string.password_cant_empty))
            }
            !isAgree -> {
                ToastUtil.show(getString(R.string.please_check_agree_agreement))
            }
            else -> {
                //校验成功
                showDialog()
                viewModel.login(userName, password)
            }
        }
    }

    /******************************************调试相关代码***********************************************/
    private var clickLogoCount = 0
    private var clickAppNameCount = 0
    private val INTERVAL = 2000L
    private var handler: Handler? = null

    private fun clickDebug(view: View) {
        if (view === binding.ivLogo) {
            clickLogoCount++
        } else if (view === binding.tvAppName) {
            clickAppNameCount++
        }
        //在2秒钟内快速点击logo与appname，各点击超过2次以上，并且加起来超过6次
        if (clickAppNameCount > 2 && clickLogoCount > 2 && clickAppNameCount + clickLogoCount > 6) {
            DebugDialog.showDialog(supportFragmentManager)
        }

        if (handler == null) {
            handler = Handler(Looper.getMainLooper())
        }
        handler?.removeCallbacksAndMessages(null)
        handler?.postDelayed({
            clickLogoCount = 0
            clickAppNameCount = 0
        }, INTERVAL)
    }
    /******************************************调试相关代码***********************************************/

}