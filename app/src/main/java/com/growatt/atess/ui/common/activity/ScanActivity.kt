package com.growatt.atess.ui.common.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.zxing.Result
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityScanBinding
import com.king.zxing.CameraScan
import com.king.zxing.DefaultCameraScan

/**
 * 扫码Activity
 */
class ScanActivity : BaseActivity(), CameraScan.OnScanResultCallback {

    companion object {

        const val KEY_CODE_TEXT = "key_code_text"

        fun start(context: Context?) {
            context?.startActivity(getIntent(context))
        }

        fun getIntent(context: Context?): Intent {
            return Intent(context, ScanActivity::class.java)
        }

    }

    private lateinit var binding: ActivityScanBinding
    private lateinit var cameraScan: CameraScan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        cameraScan = DefaultCameraScan(this, binding.previewView)
        cameraScan.setOnScanResultCallback(this)
        //启动相机预览
        cameraScan.startCamera()
    }

    private fun releaseCamera() {
        cameraScan.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }

    override fun onScanResultCallback(result: Result?): Boolean {
        result?.also {
            val intent = Intent()
            intent.putExtra(KEY_CODE_TEXT, it.text)
            setResult(RESULT_OK, intent)
            finish()
        }
        return true
    }
}