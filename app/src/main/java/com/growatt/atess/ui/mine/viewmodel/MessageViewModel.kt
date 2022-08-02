package com.growatt.atess.ui.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.growatt.atess.base.BaseViewModel
import com.growatt.atess.model.mine.MessageResultModel
import com.growatt.atess.service.http.ApiPath
import com.growatt.lib.service.http.HttpCallback
import com.growatt.lib.service.http.HttpResult
import kotlinx.coroutines.launch

/**
 * 选择国家/地区
 */
class MessageViewModel : BaseViewModel() {

    val getUnReadMsgNumLiveData = MutableLiveData<Pair<Int?, String?>>()

    /**
     * 获取未读消息数量
     */
    fun getUnReadMessageNum() {
        viewModelScope.launch {
            apiService().post(
                ApiPath.Mine.GET_MESSAGE_UNREAD_NUM,
                object : HttpCallback<HttpResult<MessageResultModel>>() {
                    override fun success(result: HttpResult<MessageResultModel>) {
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

}