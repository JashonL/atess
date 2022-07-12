package com.growatt.atess.ui.mine.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.growatt.atess.BuildConfig
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.component.image.crop.CropShape
import com.growatt.atess.component.image.crop.ImageCrop
import com.growatt.atess.databinding.ActivitySettingBinding
import com.growatt.atess.ui.common.fragment.OptionsDialog
import com.growatt.atess.ui.common.fragment.RequestPermissionHub
import com.growatt.atess.ui.mine.fragment.RegisterAccountType
import com.growatt.atess.ui.mine.viewmodel.SettingViewModel
import com.growatt.atess.util.AppUtil
import com.growatt.lib.service.account.IAccountService
import com.growatt.lib.util.ActivityBridge
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.Util
import java.io.File
import java.util.*


/**
 * 设置页面
 */
class SettingActivity : BaseActivity(), View.OnClickListener,
    IAccountService.OnUserProfileChangeListener {

    companion object {

        fun start(context: Context?) {
            context?.startActivity(Intent(context, SettingActivity::class.java))
        }

    }

    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingViewModel by viewModels()

    private var takePictureFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun setListener() {
        binding.ivAvatar.setOnClickListener(this)
        binding.itemEmail.setOnClickListener(this)
        binding.itemCancelAccount.setOnClickListener(this)
        binding.itemInstallerNo.setOnClickListener(this)
        binding.itemLanguage.setOnClickListener(this)
        binding.itemModifyPassword.setOnClickListener(this)
        binding.itemPhone.setOnClickListener(this)
        binding.btLogout.setOnClickListener(this)

        accountService().addUserProfileChangeListener(this)
    }

    private fun initView() {
        refreshUserProfile()
    }

    private fun refreshUserProfile() {
        Glide.with(this).load(accountService().userAvatar())
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.drawable.ic_default_avatar)
            .into(binding.ivAvatar)
        binding.itemUserName.setSubName(accountService().user()?.userName)
        binding.itemPhone.setSubName(accountService().user()?.phoneNum)
        binding.itemEmail.setSubName(accountService().user()?.email)
        binding.itemInstallerNo.setSubName(accountService().user()?.agentCode)
        binding.itemCancelAccount.setSubName(accountService().user()?.userName)
    }

    private fun initData() {
        viewModel.logoutLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                accountService().logout()
                accountService().login(this)
                finish()
            } else {
                ToastUtil.show(it)
            }
        }
        viewModel.uploadUserAvatarLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                accountService().saveUserAvatar(it.first)
                refreshUserProfile()
            } else {
                ToastUtil.show(it.second)
            }
        }
    }

    override fun onClick(v: View?) {
        when {
            v === binding.ivAvatar -> selectPictureMode()
            v === binding.itemEmail -> {
                ChangePhoneOrEmailActivity.start(
                    this,
                    RegisterAccountType.EMAIL
                )
            }
            v === binding.itemCancelAccount -> CancelAccountActivity.start(this)
            v === binding.itemInstallerNo -> ModifyInstallerNoActivity.start(this)
            v === binding.itemLanguage -> SelectLanguageActivity.start(this)
            v === binding.itemModifyPassword -> ModifyPasswordActivity.start(this)
            v === binding.itemPhone -> {
                ChangePhoneOrEmailActivity.start(
                    this,
                    RegisterAccountType.PHONE
                )
            }
            v === binding.btLogout -> {
                showDialog()
                viewModel.logout()
            }
        }
    }

    private fun selectPictureMode() {
        val takeAPicture = getString(R.string.take_a_picture)
        val fromTheAlbum = getString(R.string.from_the_album)
        val options =
            arrayOf(takeAPicture, fromTheAlbum)
        OptionsDialog.show(supportFragmentManager, options) {
            when (options[it]) {
                takeAPicture -> takeAPicture()
                fromTheAlbum -> fromTheAlbum()
            }
        }
    }

    private fun fromTheAlbum() {
        RequestPermissionHub.requestPermission(
            supportFragmentManager,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ) { isGranted ->
            if (isGranted) {
                ActivityBridge.startActivity(
                    this,
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
                            if (resultCode == RESULT_OK) {
                                cropImage(data?.data)
                            }
                        }
                    })
            }
        }
    }

    /**
     * Intent(MediaStore.ACTION_IMAGE_CAPTURE) 调用系统相机拍照，不需要申请Camera权限
     */
    private fun takeAPicture() {
        ActivityBridge.startActivity(
            this,
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                takePictureFile = AppUtil.createImageFile()?.apply {
                    putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(
                            this@SettingActivity,
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
                    if (resultCode == RESULT_OK) {
                        takePictureFile?.also {
                            Util.galleryAddPic(it.absolutePath)
                            cropImage(Uri.fromFile(it))
                        }
                    }
                }
            })
    }

    private fun cropImage(imageUri: Uri?) {
        imageUri?.also {
            ImageCrop.activity(it)
                .setCropShape(CropShape.CIRCLE)
                .start(this@SettingActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //剪裁图片回调
            if (requestCode == ImageCrop.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                ImageCrop.getActivityResult(data).uri?.path?.also {
                    showDialog()
                    viewModel.uploadUserAvatar(it)
                }
            }
        }
    }

    override fun onUserProfileChange(account: IAccountService) {
        refreshUserProfile()
    }

    override fun onDestroy() {
        accountService().removeUserProfileChangeListener(this)
        super.onDestroy()
    }
}