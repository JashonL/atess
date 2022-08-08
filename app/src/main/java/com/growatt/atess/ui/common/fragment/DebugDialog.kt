package com.growatt.atess.ui.common.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.growatt.atess.BuildConfig
import com.growatt.atess.application.MainApplication
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.Util

/**
 * 调试页面
 * 1.切换环境
 */
class DebugDialog : DialogFragment() {

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            DebugDialog().show(fragmentManager, DebugDialog::class.java.name)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        //测试服务器
        val testHost = "http://47.243.120.111/"
        //开发本地服务器
        val localHost = "http://20.60.5.236:8089/"
        val hostList = arrayOf(BuildConfig.apiServerUrl, testHost, localHost)
        val checkedItem = hostList.indexOf(MainApplication.instance().apiService().host())
        builder.setTitle("切换环境")
            .setSingleChoiceItems(arrayOf("正式环境", "测试环境", "本地环境"), checkedItem) { _, index ->
                MainApplication.instance().apiService().setHost(hostList[index])
                ToastUtil.show("环境切换中")
                Handler(Looper.getMainLooper()).postDelayed({
                    Util.restartApp(requireActivity())
                }, 1000)
            }
        return builder.create()
    }
}