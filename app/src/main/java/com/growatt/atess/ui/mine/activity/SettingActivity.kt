package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.growatt.atess.R
import com.growatt.atess.databinding.ActivitySettingBinding
import com.growatt.atess.ui.mine.fragment.RegisterAccountType
import com.growatt.atess.ui.mine.viewmodel.SettingViewModel
import com.growatt.lib.base.BaseActivity
import com.growatt.lib.service.account.IAccountService
import com.growatt.lib.util.ToastUtil

/**
 * 设置页面
 */
class SettingActivity : BaseActivity(), View.OnClickListener,
    IAccountService.OnUserProfileChangeListener {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, SettingActivity::class.java))
        }

    }

    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun setListener() {
        binding.ivAvatar.setOnClickListener(this)
        binding.itemEmail.setOnClickListener(this)
        binding.itemCancelAccount.setOnClickListener(this)
        binding.itemInstallerNo.setOnClickListener(this)
        binding.itemLanguage.setOnClickListener(this)
        binding.itemModifyPassword.setOnClickListener(this)
        binding.itemPhone.setOnClickListener(this)
        binding.btLogout.setOnClickListener(this)

        accountService().addUserProfileChangeListener(this)
    }

    private fun initView() {
        refreshUserProfile()
    }

    private fun refreshUserProfile() {
        Glide.with(this).load(accountService().userAvatar())
            .placeholder(R.drawable.ic_default_avatar)
            .into(binding.ivAvatar)
        binding.itemUserName.setSubName(accountService().user()?.userName)
        binding.itemPhone.setSubName(accountService().user()?.phoneNum)
        binding.itemEmail.setSubName(accountService().user()?.email)
        binding.itemInstallerNo.setSubName(accountService().user()?.agentCode)
        binding.itemCancelAccount.setSubName(accountService().user()?.userName)
    }

    private fun initData() {
        viewModel.logoutLiveData.observe(this) {
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

    override fun onClick(v: View?) {
        when {
            v === binding.ivAvatar -> {}
            v === binding.itemEmail -> {
                ChangePhoneOrEmailActivity.start(
                    this,
                    RegisterAccountType.EMAIL
                )
            }
            v === binding.itemCancelAccount -> {}
            v === binding.itemInstallerNo -> ModifyInstallerNoActivity.start(this)
            v === binding.itemLanguage -> {}
            v === binding.itemModifyPassword -> ModifyPasswordActivity.start(this)
            v === binding.itemPhone -> {
                ChangePhoneOrEmailActivity.start(
                    this,
                    RegisterAccountType.PHONE
                )
            }
            v === binding.btLogout -> {
                showDialog()
                viewModel.logout()
            }
        }
    }

    override fun onUserProfileChange(account: IAccountService) {
        refreshUserProfile()
    }

    override fun onDestroy() {
        accountService().removeUserProfileChangeListener(this)
        super.onDestroy()
    }
}