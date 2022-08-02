package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.growatt.atess.R
import com.growatt.atess.databinding.FragmentHomeMineBinding
import com.growatt.atess.ui.mine.activity.AboutActivity
import com.growatt.atess.ui.mine.activity.SettingActivity
import com.growatt.atess.ui.mine.viewmodel.MessageViewModel
import com.growatt.atess.ui.mine.viewmodel.SettingViewModel
import com.growatt.lib.service.account.IAccountService

/**
 * 首页-我的
 */
class HomeMineFragment : HomeBaseFragment(), View.OnClickListener,
    IAccountService.OnUserProfileChangeListener {
    private var _binding: FragmentHomeMineBinding? = null

    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModels()
    private val messageViewModel: MessageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMineBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        messageViewModel.getUnReadMsgNumLiveData.observe(viewLifecycleOwner) {
            if (it.second == null && it.first != null) {
                showUnReadMsgNum(it.first!!)
            }
        }
        viewModel.userAvatarLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                accountService().saveUserAvatar(it.first)
            }
        }
        viewModel.fetchUserAvatar()
    }

    private fun showUnReadMsgNum(unReadNum: Int) {
        if (unReadNum > 0) {
            binding.itemMessageCenter.showRedPoint(true)
            binding.itemMessageCenter.setRedPointText(unReadNum.toString())
        } else {
            binding.itemMessageCenter.showRedPoint(false)
        }
    }


    private fun showUserAvatar(userAvatar: String?) {
        Glide.with(this).load(userAvatar)
            .placeholder(R.drawable.ic_default_avatar)
            .into(binding.ivAvatar)
    }

    private fun setListener() {
        binding.itemSetting.setOnClickListener(this)
        binding.itemAbout.setOnClickListener(this)
        binding.itemMessageCenter.setOnClickListener(this)
        accountService().addUserProfileChangeListener(this)
    }

    private fun initView() {
        binding.tvUserName.text = accountService().user()?.userName
        showUserAvatar(accountService().userAvatar())
        showUnReadMsgNum(messageViewModel.getUnReadMsgNumLiveData.value?.first ?: 0)
    }

    override fun onClick(v: View?) {
        when {
            v === binding.itemSetting -> SettingActivity.start(context)
            v === binding.itemAbout -> AboutActivity.start(context)
            v === binding.itemMessageCenter -> {}
        }
    }

    override fun onUserProfileChange(account: IAccountService) {
        showUserAvatar(account.userAvatar())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountService().removeUserProfileChangeListener(this)
        _binding = null
    }

}