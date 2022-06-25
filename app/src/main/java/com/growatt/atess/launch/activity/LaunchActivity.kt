package com.growatt.atess.launch.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.growatt.atess.MainActivity
import com.growatt.atess.application.MainApplication
import com.growatt.atess.databinding.ActivityLaunchBinding
import com.growatt.atess.launch.fragment.UserAgreementDialog
import com.growatt.atess.launch.monitor.UserAgreementMonitor
import com.growatt.lib.base.BaseActivity
import com.growatt.lib.view.statusbar.StatusBarCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class LaunchActivity : BaseActivity() {

    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        StatusBarCompat.translucentStatusBar(this, true)
        StatusBarCompat.setWindowLightStatusBar(this, true)

        lifecycleScope.launch {
            if (fetchIsAgreeResult()) {
                enterApp()
            } else {
                finish()
            }
        }

    }

    /**
     * 获取是否同意隐私政策结果
     */
    private suspend fun fetchIsAgreeResult(): Boolean = suspendCancellableCoroutine {
        if (MainApplication.instance().isAgree()) {
            it.resumeWith(Result.success(true))
        } else {
            UserAgreementDialog.showDialog(supportFragmentManager)
            UserAgreementMonitor.watch(lifecycle) { isAgree, _ ->
                it.resumeWith(Result.success(isAgree))
            }
        }
    }

    private suspend fun enterApp() {
        delay(2000)
        MainActivity.start(this@LaunchActivity)
        finish()
    }

}