package com.growatt.atess.ui.common.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.growatt.atess.R
import com.growatt.lib.util.ToastUtil

/**
 * 权限请求工具
 */
class RequestPermissionHub : Fragment() {

    companion object {

        const val PERMISSION_REQUEST_CODE = 4896

        /**
         * 是有拥有对应的权限
         */
        fun hashPermission(context: Context, permissions: Array<String>): Boolean {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    return false
                }
            }
            return true

        }

        fun requestPermission(
            fragmentManager: FragmentManager,
            permissions: Array<String>,
            callback: ((isGranted: Boolean) -> Unit)? = null
        ) {
            val requestPermissionHub = RequestPermissionHub()
            requestPermissionHub.permissions = permissions
            requestPermissionHub.callback = callback
            fragmentManager.beginTransaction()
                .add(requestPermissionHub, RequestPermissionHub::class.java.name)
                .commitAllowingStateLoss()
        }

    }

    private lateinit var permissions: Array<String>

    /**
     * @param permission 权限名
     * @param isGranted true已授权，false未授权
     */
    private var callback: ((isGranted: Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hashPermission(requireContext(), permissions)) {
            callback?.invoke(true)
            detach()
            return
        }

        requestPermissions(permissions, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    ToastUtil.show(getString(R.string.please_to_app_setting_open_permission))
                    callback?.invoke(false)
                    return
                }
            }
            callback?.invoke(true)
            detach()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun detach() {
        parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }
}