package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentAddPlant2Binding
import com.growatt.atess.ui.plant.viewmodel.AddPlantViewModel

class AddPlant2Fragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddPlant2Binding
    private val viewModel: AddPlantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlant2Binding.inflate(inflater, container, false)
        initView()
        setListener()
        return binding.root
    }

    private fun setListener() {
        binding.tvTimezone.setOnClickListener(this)
        binding.tvCurrency.setOnClickListener(this)
        binding.tvUploadPlantImage.setOnClickListener(this)
        binding.ivPlantImage.setOnClickListener(this)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvTimezone -> {

            }
            v === binding.tvCurrency -> {

            }
            v === binding.tvUploadPlantImage -> {

            }
            v === binding.ivPlantImage -> {

            }
        }
    }

    /**
     * 保存输入框的内容
     */
    fun saveEditTextString() {
    }
}