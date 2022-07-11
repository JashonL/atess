package com.growatt.atess.ui.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentAddPlant2Binding
import com.growatt.atess.model.plant.GeneralItemModel
import com.growatt.atess.ui.common.fragment.PickerDialog
import com.growatt.atess.ui.plant.viewmodel.AddPlantViewModel
import com.growatt.lib.util.ToastUtil

class AddPlant2Fragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddPlant2Binding
    private val viewModel: AddPlantViewModel by activityViewModels()

    private var isClickSelectCurrency = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlant2Binding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.timeZoneListLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                selectTimeZone(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }

        viewModel.currencyListLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                if (isClickSelectCurrency) {
                    selectCurrency(it.first)
                } else {
                    setCurrency(it.first[0].name)
                }
            } else {
                ToastUtil.show(it.second)
            }
        }
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
                val timeZones = viewModel.timeZoneListLiveData.value?.first
                if (timeZones.isNullOrEmpty()) {
                    showDialog()
                    viewModel.fetchTimeZoneList()
                } else {
                    selectTimeZone(timeZones)
                }
            }
            v === binding.tvCurrency -> {
                val currencyList = viewModel.currencyListLiveData.value?.first
                if (currencyList.isNullOrEmpty()) {
                    isClickSelectCurrency = true
                    showDialog()
                    viewModel.fetchCurrencyList()
                } else {
                    selectCurrency(currencyList)
                }
            }
            v === binding.tvUploadPlantImage -> {

            }
            v === binding.ivPlantImage -> {

            }
        }
    }

    private fun selectCurrency(currencyList: Array<GeneralItemModel>) {
        PickerDialog.show(childFragmentManager, currencyList) {
            setCurrency(currencyList[it].name)
        }
    }

    private fun setCurrency(currency: String) {
        viewModel.addPlantModel.formulaMoneyUnitId = currency
        binding.tvCurrency.text = viewModel.addPlantModel.formulaMoneyUnitId
    }

    private fun selectTimeZone(timeZones: Array<GeneralItemModel>) {
        PickerDialog.show(childFragmentManager, timeZones) {
            viewModel.addPlantModel.plantTimeZone = timeZones[it].name
            binding.tvTimezone.text = viewModel.addPlantModel.plantTimeZone
        }
    }

    /**
     * 保存输入框的内容
     */
    fun saveEditTextString() {
        viewModel.addPlantModel.nominalPower = binding.etTotalComponentPower.text.toString().trim()
        viewModel.addPlantModel.formulaMoney = binding.etElectrovalence.text.toString().trim()
    }
}