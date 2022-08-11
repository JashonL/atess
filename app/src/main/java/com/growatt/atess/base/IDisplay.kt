package com.growatt.atess.base

import android.view.View

interface IDisplay {

    fun showDialog()

    fun dismissDialog()

    fun showPageLoadingView()

    fun hidePageLoadingView()

    fun showPageErrorView(onRetry: ((view: View) -> Unit))

    fun hidePageErrorView()

}