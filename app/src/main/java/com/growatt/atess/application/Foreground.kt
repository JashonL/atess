package com.growatt.atess.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

class Foreground private constructor() : Application.ActivityLifecycleCallbacks {

    companion object {
        private val INSTANCE by lazy(LazyThreadSafetyMode.NONE) {
            Foreground()
        }

        fun init(app: Application) {
            app.registerActivityLifecycleCallbacks(INSTANCE)
        }
    }

    private val activityList = Stack<Activity>()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

}