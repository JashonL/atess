package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.growatt.atess.BuildConfig
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityAboutBinding
import com.growatt.atess.ui.common.activity.WebActivity

/**
 * 关于页面
 */
class AboutActivity : BaseActivity(), View.OnClickListener {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, AboutActivity::class.java))
        }

    }

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListener()
    }

    private fun setListener() {
        binding.itemUserAgreement.setOnClickListener(this)
        binding.itemPrivacyPolicy.setOnClickListener(this)
        binding.itemPhone.setOnClickListener(this)
        binding.itemCompanyWebsite.setOnClickListener(this)
    }

    private fun initView() {
        binding.tvVersion.text =
            getString(R.string.version_name_format, deviceService().getAppVersionName())
        binding.itemCompanyWebsite.setSubName(BuildConfig.companyWebsite)
        binding.itemPhone.setSubName(BuildConfig.companyPhone)
    }

    override fun onClick(v: View?) {
        when {
            v === binding.itemUserAgreement -> WebActivity.start(this, BuildConfig.userAgreementUrl)
            v === binding.itemPrivacyPolicy -> WebActivity.start(this, BuildConfig.privacyPolicyUrl)
            v === binding.itemPhone -> dialPhoneNumber(binding.itemPhone.getSubName())
            v === binding.itemCompanyWebsite -> WebActivity.start(
                this,
                "https://" + BuildConfig.companyWebsite
            )
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