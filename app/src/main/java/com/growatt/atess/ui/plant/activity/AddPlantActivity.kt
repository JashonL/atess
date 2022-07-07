package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityAddPlantBinding

/**
 * 添加电站页面
 */
class AddPlantActivity : BaseActivity(), View.OnClickListener {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, AddPlantActivity::class.java))
        }

    }

    private lateinit var binding: ActivityAddPlantBinding

    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setListener()
    }

    private fun setListener() {

    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when {

        }
    }
}