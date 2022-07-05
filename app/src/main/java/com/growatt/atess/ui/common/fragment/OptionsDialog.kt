package com.growatt.atess.ui.common.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.growatt.atess.R
import com.growatt.atess.databinding.DialogOptionsBinding
import com.growatt.lib.util.ViewUtil

/**
 * 选项dialog
 */
class OptionsDialog : BottomSheetDialogFragment() {

    companion object {

        fun show(
            fragmentManager: FragmentManager,
            options: Array<String>,
            callback: ((selectPosition: Int) -> Unit)? = null
        ) {
            val dialog = OptionsDialog()
            dialog.options = options
            dialog.callback = callback
            dialog.show(fragmentManager, OptionsDialog::class.java.name)
        }

    }

    private lateinit var options: Array<String>
    private lateinit var binding: DialogOptionsBinding
    private var callback: ((selectPosition: Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding = DialogOptionsBinding.inflate(layoutInflater, null, false)
        initView()
        dialog.setContentView(binding.root)
        return dialog
    }

    private fun initView() {
        for (position in options.indices) {
            binding.llOptions.addView(generateItemView(position))
        }
        binding.tvCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    private fun generateItemView(position: Int): View {
        val textView = TextView(requireContext())
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.gravity = Gravity.CENTER
        textView.setPadding(
            0,
            ViewUtil.dp2px(requireContext(), 10f),
            0,
            ViewUtil.dp2px(requireContext(), 10f)
        )
        textView.setTextColor(resources.getColor(R.color.text_black))
        textView.textSize = 14f
        textView.text = options[position]
        textView.setOnClickListener {
            callback?.invoke(position)
            dismissAllowingStateLoss()
        }
        return textView
    }
}