package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.component.image.crop.BitmapUtils
import com.growatt.atess.databinding.ActivityAddPlantBinding
import com.growatt.atess.model.plant.AddPlantModel
import com.growatt.atess.model.plant.PlantModel
import com.growatt.atess.ui.plant.fragment.AddPlant1Fragment
import com.growatt.atess.ui.plant.fragment.AddPlant2Fragment
import com.growatt.atess.ui.plant.monitor.PlantMonitor
import com.growatt.atess.ui.plant.viewmodel.AddPlantViewModel
import com.growatt.atess.util.AppUtil
import com.growatt.lib.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 添加电站页面
 * @see com.growatt.atess.ui.plant.fragment.AddPlant1Fragment
 * @see com.growatt.atess.ui.plant.fragment.AddPlant2Fragment
 */
class AddPlantActivity : BaseActivity(), View.OnClickListener {

    companion object {

        private const val KEY_PLANT_INFO = "KEY_PLANT_INFO"

        fun start(context: Context?, plantModel: PlantModel? = null) {
            context?.startActivity(Intent(context, AddPlantActivity::class.java).apply {
                if (plantModel != null) {
                    putExtra(KEY_PLANT_INFO, plantModel)
                }
            })
        }

    }

    private lateinit var addPlant1Fragment: AddPlant1Fragment
    private lateinit var addPlant2Fragment: AddPlant2Fragment

    private lateinit var binding: ActivityAddPlantBinding

    private val viewModel: AddPlantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        viewModel.addPlantLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                AddCollectorActivity.start(this, it.first)
                PlantMonitor.notifyPlant()
                finish()
            } else {
                ToastUtil.show(it.second)
            }
        }

        viewModel.editPlantLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                PlantMonitor.notifyPlant()
                finish()
            } else {
                ToastUtil.show(it)
            }
        }

        if (intent.hasExtra(KEY_PLANT_INFO)) {
            viewModel.addPlantModel =
                intent.getParcelableExtra<PlantModel>(KEY_PLANT_INFO)?.convert() ?: AddPlantModel()
            viewModel.isEditMode = true
        } else {
            viewModel.isEditMode = false
        }
    }

    private fun setListener() {
        binding.btNextStep.setOnClickListener(this)
    }

    private fun initView() {
        if (viewModel.isEditMode) {
            binding.title.setTitleText(getString(R.string.edit_plant))
            binding.btNextStep.text = getString(R.string.finish)
        } else {
            binding.title.setTitleText(getString(R.string.add_plant))
            binding.btNextStep.text = getString(R.string.next_step)
        }

        addPlant1Fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_add_plant_1) as AddPlant1Fragment
        addPlant2Fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_add_plant_2) as AddPlant2Fragment
    }

    override fun onClick(v: View?) {
        when {
            v === binding.btNextStep -> {
                addPlant1Fragment.saveEditTextString()
                addPlant2Fragment.saveEditTextString()
                checkInput()
            }
        }
    }

    private fun checkInput() {
        val addPlantModel = viewModel.addPlantModel
        when {
            TextUtils.isEmpty(addPlantModel.plantName) -> {
                ToastUtil.show(getString(R.string.please_input_plant_name))
            }
            addPlantModel.installDate == null -> {
                ToastUtil.show(getString(R.string.please_select_install_date))
            }
            TextUtils.isEmpty(addPlantModel.country) -> {
                ToastUtil.show(getString(R.string.please_select_country_or_area_2))
            }
            TextUtils.isEmpty(addPlantModel.city) -> {
                ToastUtil.show(getString(R.string.please_select_city))
            }
            TextUtils.isEmpty(addPlantModel.plantTimeZone) -> {
                ToastUtil.show(getString(R.string.please_select_timezone))
            }
            TextUtils.isEmpty(addPlantModel.formulaMoneyUnitId) -> {
            }
            else -> {
                requestAddPlant(addPlantModel)
            }
        }
    }

    private fun requestAddPlant(addPlantModel: AddPlantModel) {
        showDialog()
        if (TextUtils.isEmpty(addPlantModel.plantFileCompress) && addPlantModel.plantFile != null
        ) {
            //执行图片压缩，压缩完成后上传到服务端
            lifecycleScope.launch {
                val async = async(Dispatchers.IO) {
                    val bitmapSampled: BitmapUtils.BitmapSampled =
                        BitmapUtils.decodeSampledBitmap(
                            this@AddPlantActivity,
                            addPlantModel.plantFile,
                            640,
                            640
                        )
                    val compressImagePath = AppUtil.saveBitmapToDisk(
                        this@AddPlantActivity,
                        bitmapSampled.bitmap
                    )
                    compressImagePath
                }
                addPlantModel.plantFileCompress = async.await()
                startRequest()
            }
        } else {
            startRequest()
        }
    }

    private fun startRequest() {
        if (viewModel.isEditMode) {
            viewModel.editPlant()
        } else {
            viewModel.addPlant()
        }
    }

}