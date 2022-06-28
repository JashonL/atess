package com.growatt.atess.ui.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.fragment.app.FragmentManager
import com.growatt.atess.R
import com.growatt.atess.databinding.DialogVerifyCodeBinding
import com.growatt.lib.base.BaseDialogFragment
import com.growatt.lib.util.setViewHeight

/**
 * 验证码弹框
 */
class VerifyCodeDialog : BaseDialogFragment(), View.OnClickListener {

    companion object {

        fun showDialog(
            fm: FragmentManager,
            @RegisterAccountType type: Int = RegisterAccountType.PHONE
        ) {
            val dialog = VerifyCodeDialog()
            dialog.type = type
            dialog.show(fm, VerifyCodeDialog::class.java.name)
        }
    }

    private lateinit var binding: DialogVerifyCodeBinding

    private var type: Int = RegisterAccountType.PHONE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogVerifyCodeBinding.inflate(inflater, container, false)
        //点击外面dialog不消失
        requireDialog().setCanceledOnTouchOutside(false)
        initView()
        setListener()
        return binding.root
    }

    private fun setListener() {
        binding.tvSendVerifyCode.setOnClickListener(this)
        binding.btCancel.setOnClickListener(this)
        binding.btConfirm.setOnClickListener(this)
        binding.tvSendVerifyCode.setOnClickListener(this)
        binding.etVerifyCode.setOnFocusChangeListener { v, hasFocus ->
            updateFocusView(hasFocus)
        }
    }

    private fun updateFocusView(hasFocus: Boolean) {
        if (hasFocus) {
            binding.etVerifyCode.setSelection(binding.etVerifyCode.length())
            binding.viewVerifyCodeLine.setBackgroundResource(R.color.colorAccent)
            binding.viewVerifyCodeLine.setViewHeight(2f)
        } else {
            binding.viewVerifyCodeLine.setBackgroundResource(R.color.color_line)
            binding.viewVerifyCodeLine.setViewHeight(1f)
        }
    }

    private fun initView() {
        val typeName =
            getString(if (type == RegisterAccountType.PHONE) R.string.phone else R.string.email)
        binding.title.text = getString(R.string.verify_format, typeName)
        binding.tvSentVerifyCodeTip.text = getString(R.string.sent_verify_code_format, typeName)
        updateFocusView(false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_cancel -> dismissAllowingStateLoss()
            R.id.bt_confirm -> {

            }
            R.id.tv_send_verify_code -> {

            }
        }
    }
}

@IntDef(RegisterAccountType.PHONE, RegisterAccountType.EMAIL)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
/**
 * 注册账号类型
 */
annotation class RegisterAccountType {
    companion object {
        const val PHONE = 0
        const val EMAIL = 1
    }
}