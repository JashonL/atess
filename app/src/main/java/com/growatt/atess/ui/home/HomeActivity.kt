package com.growatt.atess.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.growatt.atess.R
import com.growatt.atess.databinding.ActivityHomeBinding
import com.growatt.atess.ui.home.fragment.*
import com.growatt.atess.ui.home.view.HomeTab
import com.growatt.lib.base.BaseActivity

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

    private val homeFragments by lazy(LazyThreadSafetyMode.NONE) {
        mutableMapOf(
            Pair(HomeTab.SYNOPSIS, HomeSynopsisFragment()),
            Pair(HomeTab.POWER_PLANT, HomePowerPlantFragment()),
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
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.hasExtra(KEY_HOME_TAB) == true) {
            homeTab = intent.getIntExtra(KEY_HOME_TAB, HomeTab.SYNOPSIS)
        }
        switchToFragment(homeTab)
    }

    private fun initView() {
        binding.homeBottomView.setSelectPosition(homeTab)
        binding.homeBottomView.setOnTabSelectListener {
            homeTab = it
            switchToFragment(it)
        }
        switchToFragment(homeTab)
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
        outState.putInt(KEY_HOME_TAB, homeTab)
    }

}