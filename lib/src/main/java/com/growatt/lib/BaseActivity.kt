package com.growatt.lib

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {


    override fun onDestroy() {
        //取消协程，和生命周期绑定
        cancel()
        super.onDestroy()
    }


}