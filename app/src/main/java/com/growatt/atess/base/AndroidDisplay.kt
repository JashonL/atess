package com.growatt.atess.base

import android.app.ProgressDialog
import android.view.ViewGroup
import android.view.Window
import com.growatt.atess.R
import java.lang.ref.WeakReference

class AndroidDisplay(activity: BaseActivity) {

    private var progressDialog: ProgressDialog? = null

    private val wrActivity = WeakReference(activity)

    fun showDialog() {
        val activity = wrActivity.get() ?: return
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity).apply {
                setCanceledOnTouchOutside(false)
                setMessage(activity.getString(R.string.loading))
            }
        }
        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
    }

    fun dismissDialog() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }

    fun showPageLoadingView() {
        val activity = wrActivity.get() ?: return
    }

    fun hidePageLoadingView() {

    }

    fun showPageErrorView() {
        val activity = wrActivity.get() ?: return

    }

    fun hidePageErrorView() {

    }

    fun findPlaceholderContainerView(activity: BaseActivity): ViewGroup {
        val viewGroup = activity.findViewById<ViewGroup>(R.id.activity_placeholder_page)
        return viewGroup ?: return activity.findViewById(Window.ID_ANDROID_CONTENT)
    }

}