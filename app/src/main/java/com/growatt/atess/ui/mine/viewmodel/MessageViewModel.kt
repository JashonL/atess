package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.mine.MessageModel
import com.growatt.atess.model.mine.MessageUnReadNumResultModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpErrorModel
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
    val getMessageListLiveData = MutableLiveData<Pair<PageModel<MessageModel>?, String?>>()

    val deleteMessageLiveData = MutableLiveData<Pair<Int?, String?>>()

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

                    override fun onFailure(errorModel: HttpErrorModel) {
                        getUnReadMsgNumLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                    }

                })
        }
    }

    /**
     * 获取消息列表
     * @param currentPage 请求的页数
     */
    fun getMessageList(currentPage: Int) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("currentPage", currentPage.toString())
                put("pageSize", "20")
            }
            apiService().postForm(
                ApiPath.Mine.GET_MESSAGE_LIST, params,
                object : HttpCallback<HttpResult<PageModel<MessageModel>>>() {
                    override fun success(result: HttpResult<PageModel<MessageModel>>) {
                        if (result.isBusinessSuccess()) {
                            getMessageListLiveData.value = Pair(result.data, null)
                        } else {
                            getMessageListLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        getMessageListLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                    }

                })
        }
    }

    /**
     * 删除消息
     */
    fun deleteMessage(position: Int, msgId: String?) {
        viewModelScope.launch {
            val params = hashMapOf<String, String>().apply {
                put("msgId", msgId ?: "")
            }
            apiService().postForm(
                ApiPath.Mine.DELETE_MESSAGE, params,
                object : HttpCallback<HttpResult<String>>() {
                    override fun success(result: HttpResult<String>) {
                        if (result.isBusinessSuccess()) {
                            deleteMessageLiveData.value = Pair(position, null)
                        } else {
                            deleteMessageLiveData.value = Pair(null, result.msg ?: "")
                        }
                    }

                    override fun onFailure(errorModel: HttpErrorModel) {
                        deleteMessageLiveData.value = Pair(null, errorModel.errorMsg ?: "")
                    }

                })
        }
    }

}