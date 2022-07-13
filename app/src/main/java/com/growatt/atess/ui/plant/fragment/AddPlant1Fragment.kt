package com.growatt.atess.ui.plant.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentAddPlant1Binding
import com.growatt.atess.model.plant.ProvinceModel
import com.growatt.atess.ui.common.activity.AMapActivity
import com.growatt.atess.ui.common.fragment.PickerDialog
import com.growatt.atess.ui.common.fragment.RequestPermissionHub
import com.growatt.atess.ui.common.fragment.SystemLocationDisableTipDialog
import com.growatt.atess.ui.mine.activity.SelectAreaActivity
import com.growatt.atess.ui.plant.viewmodel.AddPlantViewModel
import com.growatt.lib.service.location.LocationInfo
import com.growatt.lib.service.location.OnLocationListener
import com.growatt.lib.util.ActivityBridge
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.Util
import com.growatt.lib.view.dialog.DatePickerFragment
import com.growatt.lib.view.dialog.OnDateSetListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*

/**
 * 电站名称
 * 安装日期
 * 电站地址
 */
class AddPlant1Fragment : BaseFragment(), View.OnClickListener, OnLocationListener {

    private lateinit var binding: FragmentAddPlant1Binding
    private val viewModel: AddPlantViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlant1Binding.inflate(inflater, container, false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.cityListLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                showProvinceList(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    private fun showProvinceList(provinceList: Array<ProvinceModel>) {
        if (provinceList.size == 1) {
            val cityList = provinceList[0].citys
            PickerDialog.show(childFragmentManager, cityList) {
                viewModel.addPlantModel.city = cityList[it].city
                refreshSelectCityView()
            }
        } else {
            PickerDialog.show(childFragmentManager, provinceList) { provinceIndex ->
                val cityList = provinceList[provinceIndex].citys
                PickerDialog.show(
                    childFragmentManager,
                    cityList
                ) { cityIndex ->
                    viewModel.addPlantModel.city = cityList[cityIndex].city
                    refreshSelectCityView()
                }
            }
        }
    }

    private fun setListener() {
        binding.tvInstallDate.setOnClickListener(this)
        binding.tvMapForChoosing.setOnClickListener(this)
        binding.tvAutoFetch.setOnClickListener(this)
        binding.tvSelectArea.setOnClickListener(this)
        binding.tvSelectCity.setOnClickListener(this)
    }

    private fun initView() {
        refreshInstallerDateView()
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvInstallDate -> {
                selectDate()
            }
            v === binding.tvMapForChoosing -> {
                fetchPlantAddressModeViewChange(v)
                requestLocationPermission {
                    ActivityBridge.startActivity(
                        requireActivity(),
                        AMapActivity.getIntent(requireContext()),
                        object : ActivityBridge.OnActivityForResult {
                            override fun onActivityForResult(
                                context: Context?,
                                resultCode: Int,
                                data: Intent?
                            ) {
                                if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(
                                        AMapActivity.KEY_SELECT_ADDRESS
                                    )
                                ) {
                                    val locationInfo =
                                        data.getParcelableExtra<LocationInfo>(AMapActivity.KEY_SELECT_ADDRESS)
                                    viewModel.addPlantModel.city = locationInfo?.city
                                    viewModel.addPlantModel.plantAddress = locationInfo?.address
                                    refreshLocationView()
                                }
                            }
                        })
                }
            }
            v === binding.tvAutoFetch -> {
                fetchPlantAddressModeViewChange(v)
                requestLocationPermission {
                    showDialog()
                    locationService().addLocationListener(this@AddPlant1Fragment)
                    locationService().requestLocation()
                }
            }
            v === binding.tvSelectArea -> {
                selectArea()
            }
            v === binding.tvSelectCity -> {
                selectCity()
            }
        }
    }

    private fun selectCity() {
        if (viewModel.addPlantModel.country.isNullOrEmpty()) {
            ToastUtil.show(getString(R.string.please_select_country_or_area_2))
        } else {
            val provinceList = viewModel.cityListLiveData.value?.first
            if (provinceList.isNullOrEmpty()) {
                showDialog()
                viewModel.fetchCityList(viewModel.addPlantModel.country!!)
            } else {
                showProvinceList(provinceList)
            }
        }
    }

    private fun selectArea() {
        ActivityBridge.startActivity(
            requireActivity(),
            SelectAreaActivity.getIntent(requireContext()),
            object : ActivityBridge.OnActivityForResult {
                override fun onActivityForResult(
                    context: Context?,
                    resultCode: Int,
                    data: Intent?
                ) {
                    if (resultCode == AppCompatActivity.RESULT_OK && data?.hasExtra(
                            SelectAreaActivity.KEY_AREA
                        ) == true
                    ) {
                        viewModel.addPlantModel.country =
                            data.getStringExtra(SelectAreaActivity.KEY_AREA) ?: ""
                        refreshSelectAreaView()
                        showDialog()
                        viewModel.fetchTimeZoneList(viewModel.addPlantModel.country ?: "")
                    }
                }
            })
    }

    private fun fetchPlantAddressModeViewChange(v: View) {
        binding.tvAutoFetch.setTextColor(
            resources.getColor(
                if (v === binding.tvAutoFetch) R.color.text_red else R.color.text_gray_99
            )
        )
        binding.tvAutoFetch.setBackgroundResource(if (v === binding.tvAutoFetch) R.drawable.red_background_corner_20_stroke_1 else R.drawable.gray_background_corner_20_stroke_1)
        binding.tvAutoFetch.setCompoundDrawablesWithIntrinsicBounds(
            resources.getDrawable(
                if (v === binding.tvAutoFetch) R.drawable.ic_auto_fetch else R.drawable.ic_auto_fetch_gray
            ), null, null, null
        )
        binding.tvMapForChoosing.setTextColor(
            resources.getColor(
                if (v === binding.tvMapForChoosing) R.color.text_red else R.color.text_gray_99
            )
        )
        binding.tvMapForChoosing.setBackgroundResource(if (v === binding.tvMapForChoosing) R.drawable.red_background_corner_20_stroke_1 else R.drawable.gray_background_corner_20_stroke_1)
        binding.tvMapForChoosing.setCompoundDrawablesWithIntrinsicBounds(
            resources.getDrawable(
                if (v === binding.tvMapForChoosing) R.drawable.ic_map else R.drawable.ic_map_gray
            ), null, null, null
        )
    }

    /**
     * 请求定位权限
     */
    private fun requestLocationPermission(onSuccess: () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (isSystemLocationEnable()) {
                RequestPermissionHub.requestPermission(
                    childFragmentManager,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                ) {
                    if (it) {
                        onSuccess.invoke()
                    }
                }
            }
        }
    }

    /**
     * 判断位置服务是否开启
     */
    private suspend fun isSystemLocationEnable(): Boolean =
        suspendCancellableCoroutine { continuation ->
            if (Util.isSystemLocationEnable()) {
                continuation.resumeWith(Result.success(true))
            } else {
                SystemLocationDisableTipDialog.show(childFragmentManager) {
                    continuation.resumeWith(Result.success(it))
                }
            }
        }

    private fun selectDate() {
        DatePickerFragment.show(childFragmentManager, object : OnDateSetListener {
            override fun onDateSet(date: Date) {
                viewModel.addPlantModel.installDate = date
                refreshInstallerDateView()
            }
        })
    }

    /**
     * 保存输入框的内容
     */
    fun saveEditTextString() {
        viewModel.addPlantModel.plantName = binding.etPlantName.text.toString().trim()
        viewModel.addPlantModel.plantAddress = binding.etDetailAddress.text.toString().trim()
    }

    private fun refreshLocationView() {
        refreshSelectCityView()
        refreshDetailAddressView()
    }

    private fun refreshSelectCityView() {
        binding.tvSelectCity.text = viewModel.addPlantModel.city
    }

    /**
     * 刷新国家或者地区
     */
    private fun refreshSelectAreaView() {
        binding.tvSelectArea.text = viewModel.addPlantModel.country
    }

    private fun refreshDetailAddressView() {
        binding.etDetailAddress.setText(viewModel.addPlantModel.plantAddress)
        binding.etDetailAddress.setSelection(binding.etDetailAddress.length())
    }

    private fun refreshInstallerDateView() {
        binding.tvInstallDate.text = viewModel.addPlantModel.getDateString()
    }

    override fun locationSuccess(locationInfo: LocationInfo) {
        dismissDialog()
        viewModel.addPlantModel.city = locationInfo.city
        viewModel.addPlantModel.plantAddress = locationInfo.address
        refreshLocationView()
    }

    override fun locationFailure(errorMsg: String) {
        dismissDialog()
        ToastUtil.show(errorMsg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationService().removeLocationListener(this)
    }

}