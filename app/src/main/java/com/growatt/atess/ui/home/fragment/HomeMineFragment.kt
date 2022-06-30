package com.growatt.atess.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.growatt.atess.R
import com.growatt.atess.databinding.FragmentHomeMineBinding
import com.growatt.atess.ui.mine.activity.AboutActivity
import com.growatt.atess.ui.mine.activity.SettingActivity
import com.growatt.atess.ui.mine.viewmodel.SettingViewModel

/**
 * 首页-我的
 */
class HomeMineFragment : HomeBaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeMineBinding
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMineBinding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.userAvatarLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                accountService().saveUserAvatar(it)
                showUserAvatar(it)
            }
        }
        viewModel.fetchUserAvatar()
    }

    private fun showUserAvatar(userAvatar: String?) {
        Glide.with(this).load(userAvatar)
            .placeholder(R.drawable.ic_default_avatar)
            .into(binding.ivAvatar)
    }

    private fun setListener() {
        binding.itemSetting.setOnClickListener(this)
        binding.itemAbout.setOnClickListener(this)
    }

    private fun initView() {
        binding.tvUserName.text = accountService().user()?.userName
        showUserAvatar(accountService().userAvatar())
    }

    override fun onClick(v: View?) {
        when {
            v === binding.itemSetting -> SettingActivity.start(context)
            v === binding.itemAbout -> AboutActivity.start(context)
        }
    }

}