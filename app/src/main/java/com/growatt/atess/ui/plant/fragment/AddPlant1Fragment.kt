package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentAddPlant1Binding
import com.growatt.atess.ui.plant.viewmodel.AddPlantViewModel
import com.growatt.lib.view.dialog.DatePickerFragment
import com.growatt.lib.view.dialog.OnDateSetListener
import java.util.*

class AddPlant1Fragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddPlant1Binding
    private val viewModel: AddPlantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlant1Binding.inflate(inflater, container, false)
        initView()
        setListener()
        return binding.root
    }

    private fun setListener() {
        binding.tvInstallDate.setOnClickListener(this)
        binding.tvMapForChoosing.setOnClickListener(this)
        binding.tvAutoFetch.setOnClickListener(this)
        binding.tvSelectArea.setOnClickListener(this)
        binding.tvSelectCity.setOnClickListener(this)
    }

    private fun initView() {
        binding.tvInstallDate.text = viewModel.getDateString()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvInstallDate -> {
                selectDate()
            }
            v === binding.tvMapForChoosing -> {

            }
            v === binding.tvAutoFetch -> {

            }
            v === binding.tvSelectArea -> {

            }
            v === binding.tvSelectCity -> {

            }
        }
    }

    private fun selectDate() {
        DatePickerFragment.show(childFragmentManager, object : OnDateSetListener {
            override fun onDateSet(date: Date) {
                viewModel.date = date
                binding.tvInstallDate.text = viewModel.getDateString()
            }
        })
    }

    /**
     * 保存输入框的内容
     */
    fun saveEditTextString() {
        viewModel.plantName = binding.etPlantName.text.toString().trim()
    }


}