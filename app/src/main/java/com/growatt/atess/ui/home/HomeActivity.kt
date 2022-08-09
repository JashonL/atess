package com.growatt.atess.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityHomeBinding
import com.growatt.atess.ui.home.fragment.*
import com.growatt.atess.ui.home.view.HomeTab
import com.growatt.atess.ui.mine.viewmodel.MessageViewModel
import com.growatt.lib.util.ToastUtil

class HomeActivity : BaseActivity() {

    companion object {

        private const val KEY_HOME_TAB = "key_home_tab"

        fun start(context: Context, @HomeTab homeTab: Int = HomeTab.SYNOPSIS) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(KEY_HOME_TAB, homeTab)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityHomeBinding

    private var currentShowFragment: HomeBaseFragment? = null

    private val messageViewModel: MessageViewModel by viewModels()

    private val homeFragments by lazy(LazyThreadSafetyMode.NONE) {
        mutableMapOf(
            Pair(HomeTab.SYNOPSIS, HomeSynopsisFragment()),
            Pair(HomeTab.PLANT, HomePlantFragment()),
            Pair(HomeTab.SERVICE, HomeServiceFragment()),
            Pair(HomeTab.MINE, HomeMineFragment()),
        )
    }

    @HomeTab
    private var homeTab: Int = HomeTab.SYNOPSIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData(savedInstanceState)
        initView()
    }

    private fun initData(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            homeTab = intent.getIntExtra(KEY_HOME_TAB, HomeTab.SYNOPSIS)
        } else {
            homeTab = savedInstanceState.getInt(KEY_HOME_TAB)
        }
        messageViewModel.getUnReadMsgNumLiveData.observe(this) {
            if (it.second == null) {
                if (it.first ?: 0 > 0) {
                    binding.homeBottomView.showRedPoint(HomeTab.MINE, it.first?.toString())
                } else {
                    binding.homeBottomView.hideRedPoint(HomeTab.MINE)
                }
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.hasExtra(KEY_HOME_TAB) == true) {
            homeTab = intent.getIntExtra(KEY_HOME_TAB, HomeTab.SYNOPSIS)
        }
        switchToFragment(homeTab)
    }

    override fun onResume() {
        super.onResume()
        messageViewModel.getUnReadMessageNum()
    }

    private fun initView() {
        setHomeBottomPosition(homeTab)
        binding.homeBottomView.setOnTabSelectListener {
            homeTab = it
            switchToFragment(it)
        }
        switchToFragment(homeTab)
    }

    fun setHomeBottomPosition(@HomeTab homeTab: Int) {
        binding.homeBottomView.setSelectPosition(homeTab)
    }

    private fun switchToFragment(@HomeTab homeTab: Int) {
        val needToShowFragment = homeFragments[homeTab]!!
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (currentShowFragment != null) {
            beginTransaction.hide(currentShowFragment!!)
        }
        if (supportFragmentManager.fragments.contains(needToShowFragment)) {
            beginTransaction.show(needToShowFragment)
        } else {
            beginTransaction.add(R.id.fl_flagments, needToShowFragment).show(needToShowFragment)

        }
        beginTransaction.commitAllowingStateLoss()
        currentShowFragment = needToShowFragment
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //不保存fragment状态，当系统资源不足的时候，activity被销毁，不保存fragment数据，解决恢复状态崩溃问题
        outState.remove("android:support:fragments")
        outState.remove("android:fragments")
        val bundle = outState.get("androidx.lifecycle.BundlableSavedStateRegistry.key")
        if (bundle is Bundle) {
            //适配高版本
            bundle.remove("android:support:fragments")
            bundle.remove("android:fragments")
        }

        outState.putInt(KEY_HOME_TAB, homeTab)
    }

}