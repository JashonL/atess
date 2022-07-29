package com.growatt.atess.ui.plant.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentPlantWeatherBinding
import com.growatt.atess.model.plant.WeatherModel
import com.growatt.atess.ui.plant.viewmodel.PlantInfoViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 电站详情-天气信息
 */
class PlantWeatherFragment : BaseFragment() {

    private var _binding: FragmentPlantWeatherBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PlantInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantWeatherBinding.inflate(inflater, container, false)
        initData()
        initView()
        return binding.root
    }

    private fun initView() {
        binding.root.gone()
    }

    private fun initData() {
        viewModel.getPlantWeatherInfoLiveData.observe(viewLifecycleOwner) {
            if (it.second == null) {
                showData(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun showData(weatherModel: WeatherModel?) {
        weatherModel?.also {
            if (it.success) {
                Glide.with(this).load(it.icon)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(binding.ivWeather)
                binding.tvTemperature.text = it.newTmp
                binding.tvCityAndWeather.text = it.getCityAndWeatherText()
                binding.tvSunrise.text = it.sr
                binding.tvSunset.text = it.ss
                binding.tvSunshineDurationValue.text = it.duration
                binding.tvWindDirection.text = it.getWindDirectionText()
                binding.tvWindSpeed.text = it.getWindSpeedText()
                binding.root.visible()
            } else {
                binding.root.gone()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}