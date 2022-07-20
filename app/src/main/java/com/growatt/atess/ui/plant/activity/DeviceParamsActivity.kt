package com.growatt.atess.ui.plant.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import com.growatt.atess.R
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.databinding.ActivityDeviceParamsBinding
import com.growatt.lib.util.ViewUtil
import org.json.JSONObject

/**
 * 设备-全部参数
 */
class DeviceParamsActivity : BaseActivity() {

    companion object {

        private val KEY_JSON_STRING = "key_json_string"

        fun start(context: Context?, jsonString: String) {
            context?.startActivity(Intent(context, DeviceParamsActivity::class.java).also {
                it.putExtra(KEY_JSON_STRING, jsonString)
            })
        }

    }

    private lateinit var binding: ActivityDeviceParamsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceParamsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }


    private fun initView() {
        val jsonStr = intent.getStringExtra(KEY_JSON_STRING)
        jsonStr?.also {
            val json = JSONObject(jsonStr)
            json.keys().forEach {
                binding.llParams.addView(generateItemView(it, json.getString(it)))
            }
        }
    }

    private fun generateItemView(key: String, value: String?): View {
        return LinearLayout(this).also {
            it.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewUtil.dp2px(this, 50f)
            )
            it.gravity = Gravity.CENTER_VERTICAL
            it.addView(generateText(R.color.text_gray_99, key))
            if (!TextUtils.isEmpty(value)) {
                it.addView(generateText(R.color.text_blue, value!!))
            }
        }
    }

    private fun generateText(@ColorRes colorResId: Int, text: String): View {
        return TextView(this).also {
            it.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_medium))
            it.setTextColor(resources.getColor(colorResId))
            it.text = text
            it.gravity = Gravity.CENTER_VERTICAL
            it.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            it.setPadding(ViewUtil.dp2px(this, 20f), 0, 0, 0)
        }

    }
}