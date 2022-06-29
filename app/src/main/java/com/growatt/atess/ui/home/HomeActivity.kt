package com.growatt.atess.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.growatt.atess.databinding.ActivityHomeBinding
import com.growatt.lib.base.BaseActivity

class HomeActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}