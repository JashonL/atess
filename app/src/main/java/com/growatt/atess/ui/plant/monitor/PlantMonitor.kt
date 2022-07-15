package com.growatt.atess.ui.plant.monitor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.growatt.atess.application.MainApplication

/**
 * 电站修改监听
 * 1.新增电站
 * 2.编辑电站
 * 3.删除电站
 */
class PlantMonitor : BroadcastReceiver(), LifecycleObserver {

    private var listener: ((monitor: PlantMonitor) -> Unit)? = null

    companion object {

        private const val ACTION_PLANT = "action_plant"

        /**
         * 使用高阶函数，将函数作为参数传进去，减少创建接口
         */
        fun watch(
            lifecycle: Lifecycle? = null,
            listener: (monitor: PlantMonitor) -> Unit
        ): PlantMonitor {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ACTION_PLANT)
            val monitor = PlantMonitor()
            monitor.listener = listener
            lifecycle?.addObserver(monitor)
            LocalBroadcastManager.getInstance(MainApplication.instance())
                .registerReceiver(monitor, intentFilter)
            return monitor
        }

        fun notifyPlant() {
            LocalBroadcastManager.getInstance(MainApplication.instance())
                .sendBroadcast(Intent().apply {
                    action = ACTION_PLANT
                })
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == ACTION_PLANT) {
                listener?.invoke(this)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unWatch() {
        listener = null
        LocalBroadcastManager.getInstance(MainApplication.instance()).unregisterReceiver(this)
    }
}