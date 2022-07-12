package com.growatt.atess.ui.plant.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.growatt.atess.BuildConfig
import com.growatt.atess.R
import com.growatt.atess.base.BaseFragment
import com.growatt.atess.databinding.FragmentAddPlant2Binding
import com.growatt.atess.model.plant.GeneralItemModel
import com.growatt.atess.ui.common.fragment.OptionsDialog
import com.growatt.atess.ui.common.fragment.PickerDialog
import com.growatt.atess.ui.common.fragment.RequestPermissionHub
import com.growatt.atess.ui.plant.viewmodel.AddPlantViewModel
import com.growatt.atess.util.AppUtil
import com.growatt.lib.util.*
import java.io.File

/**
 * 时区
 * 组件总功率
 * 电站类型
 * 资金收益
 * 电站图片
 */
class AddPlant2Fragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddPlant2Binding
    private val viewModel: AddPlantViewModel by activityViewModels()

    private var isClickSelectCurrency = false
    private var takePictureFile: File? = null

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
        viewModel.timeZoneLiveData.observe(viewLifecycleOwner) {
            dismissDialog()
            if (it.second == null) {
                viewModel.addPlantModel.formulaMoneyUnitId = it.first?.monetaryUnit
                binding.tvCurrency.text = it.first?.monetaryUnit
                viewModel.addPlantModel.plantTimeZone = it.first?.timezoneList?.get(0)
                binding.tvTimezone.text = viewModel.addPlantModel.plantTimeZone
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
        if (viewModel.addPlantModel.plantFile == null) {
            binding.ivPlantImage.gone()
            binding.tvUploadPlantImage.visible()
        } else {
            binding.ivPlantImage.visible()
            binding.tvUploadPlantImage.gone()
            Glide.with(this).load(viewModel.addPlantModel.plantFile)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.ivPlantImage)
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.tvTimezone -> {
                val timeZone = viewModel.timeZoneLiveData.value?.first
                selectTimeZone(timeZone?.getGeneralItem())
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
                selectPictureMode()
            }
            v === binding.ivPlantImage -> {
                selectPictureMode()
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

    private fun selectTimeZone(timeZones: Array<GeneralItemModel>?) {
        if (timeZones.isNullOrEmpty()) {
            return
        }
        PickerDialog.show(childFragmentManager, timeZones) {
            viewModel.addPlantModel.plantTimeZone = timeZones[it].name
            binding.tvTimezone.text = viewModel.addPlantModel.plantTimeZone
        }
    }

    private fun selectPictureMode() {
        val takeAPicture = getString(R.string.take_a_picture)
        val fromTheAlbum = getString(R.string.from_the_album)
        val options =
            arrayOf(takeAPicture, fromTheAlbum)
        OptionsDialog.show(childFragmentManager, options) {
            when (options[it]) {
                takeAPicture -> takeAPicture()
                fromTheAlbum -> fromTheAlbum()
            }
        }
    }

    private fun fromTheAlbum() {
        RequestPermissionHub.requestPermission(
            childFragmentManager,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ) { isGranted ->
            if (isGranted) {
                ActivityBridge.startActivity(
                    requireActivity(),
                    Intent(Intent.ACTION_PICK).apply {
                        type = "image/*"
                        data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    },
                    object :
                        ActivityBridge.OnActivityForResult {
                        override fun onActivityForResult(
                            context: Context?,
                            resultCode: Int,
                            data: Intent?
                        ) {
                            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                                setPlantImage(data.dataString)
                            }
                        }
                    })
            }
        }
    }

    /**
     * 设置圆角与CenterCrop，需要在代码中进行设置，还需要注意顺序
     */
    private fun setPlantImage(file: String?) {
        viewModel.addPlantModel.plantFile = file
        Glide.with(this).load(file)
            .apply(
                RequestOptions().transform(
                    CenterCrop(), RoundedCorners(ViewUtil.dp2px(requireContext(), 2f))
                )
            )
            .into(binding.ivPlantImage)
        binding.ivPlantImage.visible()
        binding.tvUploadPlantImage.gone()
    }

    private fun takeAPicture() {
        ActivityBridge.startActivity(
            requireActivity(),
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                takePictureFile = AppUtil.createImageFile()?.apply {
                    putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(
                            requireContext(),
                            BuildConfig.APPLICATION_ID + ".fileProvider",
                            this
                        )
                    )
                }
            },
            object :
                ActivityBridge.OnActivityForResult {
                override fun onActivityForResult(
                    context: Context?,
                    resultCode: Int,
                    data: Intent?
                ) {
                    if (resultCode == AppCompatActivity.RESULT_OK) {
                        takePictureFile?.also {
                            Util.galleryAddPic(it.absolutePath)
                            setPlantImage(it.absolutePath)
                        }
                    }
                }
            })
    }

    /**
     * 保存输入框的内容
     */
    fun saveEditTextString() {
        viewModel.addPlantModel.nominalPower = binding.etTotalComponentPower.text.toString().trim()
        viewModel.addPlantModel.formulaMoney = binding.etElectrovalence.text.toString().trim()
    }

}