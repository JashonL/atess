package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.viewModels
import com.growatt.atess.BuildConfig
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityCancelAccountBinding
import com.growatt.atess.ui.common.WebActivity
import com.growatt.atess.ui.mine.viewmodel.SettingViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 注销账号页面
 */
class CancelAccountActivity : BaseActivity(), View.OnClickListener {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, CancelAccountActivity::class.java))
        }

    }

    private lateinit var binding: ActivityCancelAccountBinding

    private val settingViewModel: SettingViewModel by viewModels()

    private var isAgree = false

    //是否第一次点击按钮
    private var isFirstClickButton = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        settingViewModel.cancelAccountLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                accountService().logout()
                accountService().login(this)
                finish()
            } else {
                ToastUtil.show(it)
            }
        }
    }

    private fun setListener() {
        binding.btCancel.setOnClickListener(this)
        binding.ivSelect.setOnClickListener(this)
    }

    private fun initView() {
        if (isFirstClickButton) {
            binding.tvCancelAccountTip.gone()
            binding.llClickCancelNotice.gone()
        } else {
            binding.tvCancelAccountTip.visible()
            binding.llClickCancelNotice.visible()
            updateSelectView(false)
            binding.tvClickCancelNotice.text = getTvSpan()
            binding.tvClickCancelNotice.run {
                highlightColor = resources.getColor(android.R.color.transparent)
                movementMethod = LinkMovementMethod.getInstance()
                text = getTvSpan()
            }
        }
        binding.tvUserName.text = accountService().user()?.userName
    }

    private fun updateSelectView(isAgree: Boolean) {
        binding.ivSelect.setImageResource(if (isAgree) R.drawable.ic_select else R.drawable.ic_unselect)
        this.isAgree = isAgree
        binding.btCancel.isEnabled = isAgree
    }

    private fun getTvSpan(): SpannableString {
        val notice = getString(R.string.notice_for_account_cancellation)
        val content = getString(R.string.notice_for_account_cancellation_foramt, notice)
        return SpannableString(content).apply {
            addColorSpan(this, notice)
            addClickSpan(this, notice) {
                WebActivity.start(this@CancelAccountActivity, BuildConfig.noticeForCancelAccountUrl)
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
            v === binding.ivSelect -> updateSelectView(!isAgree)
            v === binding.btCancel -> {
                if (isFirstClickButton) {
                    isFirstClickButton = false
                    initView()
                } else {
                    showDialog()
                    settingViewModel.cancelAccount()
                }
            }
        }
    }

    /**
     * 调起拨号键盘并把号码传过去，用户需要点击拨号按钮才会拨打电话
     */
    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}