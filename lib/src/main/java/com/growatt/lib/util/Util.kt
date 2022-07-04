package com.growatt.lib.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Process
import kotlin.system.exitProcess


object Util {

    fun getProcessNameByPID(context: Context, pid: Int): String? {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }

    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    fun backToDesktop(context: Context?) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        context?.startActivity(intent)
    }
}