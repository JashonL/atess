package com.growatt.atess.launch.activity

import android.os.Bundle
import com.growatt.atess.databinding.ActivityLaunchBinding
import com.growatt.lib.base.BaseActivity

class LaunchActivity : BaseActivity() {

    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}