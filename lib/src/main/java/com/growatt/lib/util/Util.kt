package com.growatt.lib.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Process
import com.growatt.lib.LibApplication
import java.io.File
import kotlin.system.exitProcess


object Util {

    /**
     * 获取包名（进程名）
     */
    fun getProcessNameByPID(context: Context, pid: Int): String? {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }

    /**
     * 重启app
     */
    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    /**
     * 返回桌面
     */
    fun backToDesktop(context: Context?) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        context?.startActivity(intent)
    }

    /**
     * 判断系统定位功能是否开启
     * @return true 表示开启
     */
    fun isSystemLocationEnable(): Boolean {
        return try {
            val locationManager = LibApplication.instance()
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            gps || network
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 将图片添加到手机相册
     */
    fun galleryAddPic(currentPhotoPath: String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            LibApplication.instance().sendBroadcast(mediaScanIntent)
        }
    }
}