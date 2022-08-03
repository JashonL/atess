package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.mine.MessageModel
import com.growatt.atess.model.mine.MessageUnReadNumResultModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import com.growatt.lib.service.http.PageModel
import kotlinx.coroutines.launch

/**
 * 消息
 */
class MessageViewModel : BaseViewModel() {

    val getUnReadMsgNumLiveData = MutableLiveData<Pair<Int?, String?>>()

    /**
     * 是否是最后一页
     */
    val getMessageListLiveData = MutableLiveData<Triple<Array<MessageModel>?, Boolean, String?>>()

    var currentPage = 0

    val deleteMessageLiveData = MutableLiveData<String?>()

    /**
     * 获取未读消息数量
     */
    fun getUnReadMessageNum() {
        viewModelScope.launch {
            apiService().post(
                ApiPath.Mine.GET_MESSAGE_UNREAD_NUM,
                object : HttpCallback<HttpResult<MessageUnReadNumResultModel>>() {
                    override fun success(result: HttpResult<MessageUnReadNumResultModel>) {
                        if (result.isBusinessSuccess()) {
                            getUnReadMsgNumLiveData.value = Pair(result.data?.unReadMsgNum, null)
                        } else {
                            getUnReadMsgNumLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        getUnReadMsgNumLiveData.value = Pair(null, error ?: "")
                    }
                })
        }
    }

    /**
     * 获取消息列表
     * @param isRefresh 是否是刷新
     */
    fun getMessageList(isRefresh: Boolean) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                if (isRefresh) {
                    put("currentPage", "1")
                } else {
                    put("currentPage", (currentPage + 1).toString())
                }
                put("pageSize", "20")
            }
            apiService().postForm(
                ApiPath.Mine.GET_MESSAGE_LIST, params,
                object : HttpCallback<HttpResult<PageModel<MessageModel>>>() {
                    override fun success(result: HttpResult<PageModel<MessageModel>>) {
                        if (result.isBusinessSuccess()) {
                            currentPage = result.data?.currentPage ?: 1
                            val isLastPage = result.data?.isLastPage() ?: false
                            getMessageListLiveData.value =
                                Triple(result.data?.list ?: emptyArray(), isLastPage, null)
                        } else {
                            getMessageListLiveData.value = Triple(null, false, result.msg ?: "")
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        getMessageListLiveData.value = Triple(null, false, error ?: "")
                    }
                })
        }
    }

    /**
     * 删除消息
     */
    fun deleteMessage(msgId: String?) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("msgId", msgId ?: "")
            }
            apiService().postForm(
                ApiPath.Mine.DELETE_MESSAGE, params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            deleteMessageLiveData.value = null
                        } else {
                            deleteMessageLiveData.value = result.msg ?: ""
                        }
                    }

                    override fun onFailure(error: String?) {
                        super.onFailure(error)
                        deleteMessageLiveData.value = error ?: ""
                    }
                })
        }
    }

}