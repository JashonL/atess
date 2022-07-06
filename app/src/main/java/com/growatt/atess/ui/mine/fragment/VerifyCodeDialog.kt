package com.growatt.atess.ui.mine.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.growatt.atess.R
import com.growatt.atess.base.BaseDialogFragment
import com.growatt.atess.databinding.DialogVerifyCodeBinding
import com.growatt.atess.ui.mine.viewmodel.VerifyCodeViewModel
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.setViewHeight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 验证码弹框
 */
class VerifyCodeDialog : BaseDialogFragment(), View.OnClickListener {

    companion object {

        val FRAGMENT_TAG = VerifyCodeDialog::class.java.name

        fun showDialog(
            fm: FragmentManager,
            remainingTime: Int,
            phoneOrEmail: String,
            @RegisterAccountType type: Int = RegisterAccountType.PHONE,
            onVerifySuccess: (verifyCode: String) -> Unit
        ) {
            val dialog = VerifyCodeDialog()
            dialog.type = type
            dialog.remainingTime = remainingTime
            dialog.phoneOrEmail = phoneOrEmail
            dialog.onVerifySuccess = onVerifySuccess
            dialog.show(fm, FRAGMENT_TAG)
        }

        fun isShowing(fm: FragmentManager): Boolean {
            return fm.findFragmentByTag(FRAGMENT_TAG) != null
        }
    }

    private lateinit var binding: DialogVerifyCodeBinding
    private lateinit var onVerifySuccess: (verifyCode: String) -> Unit

    private var type: Int = RegisterAccountType.PHONE
    private lateinit var phoneOrEmail: String
    private var remainingTime = 0
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity()).get(VerifyCodeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogVerifyCodeBinding.inflate(inflater, container, false)
        //点击外面dialog不消失
        requireDialog().setCanceledOnTouchOutside(false)
        initData()
        initView()
        setListener()
        return binding.root
    }

    private fun initData() {
        viewModel.getVerifyCodeLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                updateCountDown(it.first)
            } else {
                ToastUtil.show(it.second)
            }
        }

        viewModel.verifyCodeLiveData.observe(this) {
            dismissDialog()
            if (it == null) {
                val verifyCode = binding.etVerifyCode.text.toString().trim()
                onVerifySuccess.invoke(verifyCode)
            } else {
                ToastUtil.show(it)
            }
            dismissAllowingStateLoss()
        }
    }

    private fun setListener() {
        binding.tvSendVerifyCode.setOnClickListener(this)
        binding.btCancel.setOnClickListener(this)
        binding.btConfirm.setOnClickListener(this)
        binding.tvSendVerifyCode.setOnClickListener(this)
        binding.etVerifyCode.setOnFocusChangeListener { _, hasFocus ->
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
        binding.tvPhoneOrEmail.text = phoneOrEmail
        updateFocusView(false)
        updateCountDown(remainingTime)
    }

    private fun updateCountDown(remainingTime: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (remainingTime > 0) {
                binding.tvSendVerifyCode.isEnabled = false
                for (i in remainingTime downTo 0) {
                    if (i == 0) {
                        binding.tvSendVerifyCode.isEnabled = true
                        binding.tvSendVerifyCode.text = getString(R.string.send_verify_code)
                    } else {
                        binding.tvSendVerifyCode.text = getString(R.string.second_after_send, i)
                        delay(1000)
                    }
                }
            } else {
                binding.tvSendVerifyCode.isEnabled = true
                binding.tvSendVerifyCode.text = getString(R.string.send_verify_code)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_cancel -> dismissAllowingStateLoss()
            R.id.bt_confirm -> verifyCode()
            R.id.tv_send_verify_code -> {
                showDialog()
                viewModel.fetchVerifyCode(phoneOrEmail, type)
            }
        }
    }

    private fun verifyCode() {
        val verifyCode = binding.etVerifyCode.text.toString().trim()
        if (!TextUtils.isEmpty(verifyCode) && verifyCode.length == 6) {
            showDialog()
            viewModel.verifyCode(phoneOrEmail, verifyCode)

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
        /**
         * 电话
         */
        const val PHONE = 0

        /**
         * 邮箱
         */
        const val EMAIL = 1
    }
}