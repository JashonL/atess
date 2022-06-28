package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.growatt.atess.databinding.ActivitySelectAreaBinding
import com.growatt.lib.base.BaseActivity

/**
 * 选择国家/地区页面
 */
class SelectAreaActivity : BaseActivity(), View.OnClickListener {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, SelectAreaActivity::class.java))
        }
    }

    private lateinit var binding: ActivitySelectAreaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListener()
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener(this)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivBack -> {
                finish()
            }
        }
    }

}