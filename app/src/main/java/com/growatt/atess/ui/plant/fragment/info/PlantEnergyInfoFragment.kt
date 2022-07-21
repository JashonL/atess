package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantEnergyInfoBinding
import com.growatt.atess.databinding.ItemEnergyInfoBinding

/**
 * 电站详情-能源概况
 */
class PlantEnergyInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentPlantEnergyInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantEnergyInfoBinding.inflate(inflater, container, false)
        initData()
        initView()
        return binding.root
    }

    private fun initData() {

    }

    private fun initView() {

    }

    fun generateItemView(iconUrl: String, name: String, day: String, total: String) {
        val itemBinding = ItemEnergyInfoBinding.inflate(layoutInflater, binding.llContainer, false)
        Glide.with(this).load(iconUrl).into(itemBinding.ivIcon)
        itemBinding.tvName.text = name
        itemBinding.tvPhotovoltaicOutputDay.text = day
        itemBinding.tvPhotovoltaicOutputTotal.text = total
    }
}