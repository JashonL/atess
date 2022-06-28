package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.growatt.atess.R
import com.growatt.atess.databinding.ActivityRegisterBinding
import com.growatt.atess.ui.mine.fragment.VerifyCodeDialog
import com.growatt.lib.base.BaseActivity
import com.growatt.lib.util.ToastUtil

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
    private var isAgree = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListener()
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener(this)
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
    }

    private fun updateSelectView(isAgree: Boolean) {
        binding.ivSelect.setImageResource(if (isAgree) R.drawable.ic_select else R.drawable.ic_unselect)
        this.isAgree = isAgree
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
            v === binding.ivBack -> finish()
            v === binding.tvSelectArea -> {}
            v === binding.ivSelect -> {
                updateSelectView(!isAgree)
            }
            v === binding.btFinish -> {
                VerifyCodeDialog.showDialog(supportFragmentManager)
            }
        }
    }

}