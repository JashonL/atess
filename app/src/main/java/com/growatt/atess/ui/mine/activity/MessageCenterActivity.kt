package com.growatt.atess.ui.mine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.growatt.atess.R
import com.growatt.atess.application.MainApplication
import com.growatt.atess.base.BaseActivity
import com.growatt.atess.base.BasePageListAdapter
import com.growatt.atess.base.BaseViewHolder
import com.growatt.atess.databinding.ActivityMessageCenterBinding
import com.growatt.atess.databinding.MessageViewHolderBinding
import com.growatt.atess.model.mine.MessageModel
import com.growatt.atess.ui.mine.viewmodel.MessageViewModel
import com.growatt.atess.view.dialog.AlertDialog
import com.growatt.atess.view.itemdecoration.DividerItemDecoration
import com.growatt.lib.util.ToastUtil
import com.growatt.lib.util.gone
import com.growatt.lib.util.visible

/**
 * 消息中心
 */
class MessageCenterActivity : BaseActivity() {

    companion object {

        fun start(context: Context?) {
            if (MainApplication.instance().accountService().isGuest()) {
                ToastUtil.show(
                    MainApplication.instance().getString(R.string.info_space_not_permission)
                )
            } else {
                context?.startActivity(Intent(context, MessageCenterActivity::class.java))
            }
        }

    }

    private lateinit var binding: ActivityMessageCenterBinding
    private val viewModel: MessageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
        setListener()
    }

    private fun initData() {
        viewModel.getMessageListLiveData.observe(this) {
            if (it.second == null) {
                getAdapter().setResultSuccess(it.first!!)
            } else {
                ToastUtil.show(it.second)
                getAdapter().setResultError()
            }
        }
        viewModel.deleteMessageLiveData.observe(this) {
            dismissDialog()
            if (it.second == null) {
                getAdapter().removePosition(it.first!!)
            } else {
                ToastUtil.show(it.second)
            }
        }
        binding.srlRefresh.autoRefresh()
    }

    private fun setListener() {
        binding.srlRefresh.setOnRefreshListener {
            (binding.rvMessageList.adapter as Adapter).refresh()
        }
    }

    private fun getAdapter(): Adapter {
        return binding.rvMessageList.adapter as Adapter
    }

    private fun initView() {
        binding.rvMessageList.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL,
                resources.getColor(R.color.color_line)
            )
        )
        binding.rvMessageList.adapter = Adapter().also {
            it.refreshView = binding.srlRefresh
        }
    }

    inner class Adapter : BasePageListAdapter<MessageModel>() {

        override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return MessageViewHolder.create(parent) { view, position ->
                AlertDialog.showDialog(
                    supportFragmentManager,
                    getString(R.string.delete_message_or_not)
                ) {
                    showDialog()
                    viewModel.deleteMessage(position, dataList[position].id)
                }
            }
        }

        override fun onBindItemViewHolder(holder: BaseViewHolder, position: Int) {
            if (holder is MessageViewHolder) {
                holder.bindData(dataList[position])
            }
        }

        override fun onLoadNext() {
            viewModel.getMessageList(currentPage + 1)
        }

        override fun onRefresh() {
            viewModel.getMessageList(1)
        }

        override fun showEmptyView() {
            binding.tvEmpty.visible()
        }

        override fun hideEmptyView() {
            binding.tvEmpty.gone()
        }

    }

}

class MessageViewHolder(
    itemView: View,
) : BaseViewHolder(itemView) {

    companion object {
        fun create(
            parent: ViewGroup,
            delete: (View, Int) -> Unit
        ): MessageViewHolder {
            val binding = MessageViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val holder = MessageViewHolder(binding.root)
            holder.binding = binding
            binding.ivDelete.setOnClickListener {
                delete.invoke(it, holder.absoluteAdapterPosition)
            }
            return holder
        }
    }

    private lateinit var binding: MessageViewHolderBinding

    fun bindData(message: MessageModel) {
        binding.message = message
    }
}