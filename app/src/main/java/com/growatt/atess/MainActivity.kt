package com.growatt.atess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.growatt.atess.databinding.ActivityMainBinding
import com.growatt.lib.base.BaseActivity

class MainActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}