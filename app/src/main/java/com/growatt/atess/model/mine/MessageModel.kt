package com.growatt.atess.model.mine

/**
 *消息中心
"id": 6005, //消息ID
"time": "2022-07-23 16:44", //故障时间
"title": "故障信息", // 标题
"faultContent": "序列号：NVCRBK107J(NVCRBK107J)的设备发生故障，请检查！", //故障信息
"plantName": "智能电表测试", //电站名称
"deviceType": "inv", //设备类型
"deviceSn": "NVCRBK107J" // 设备序列号
 */
data class MessageModel(
    val id: String,
    val time: String,
    val title: String,
    val plantName: String,
    val deviceSn: String,
    val deviceType: String,
    val faultContent: String
)

data class MessageResultModel(val unReadMsgNum: Int, val messageList: Array<MessageModel>?) {

    fun getSafeMessageList(): Array<MessageModel> {
        return messageList ?: emptyArray()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageResultModel

        if (unReadMsgNum != other.unReadMsgNum) return false
        if (messageList != null) {
            if (other.messageList == null) return false
            if (!messageList.contentEquals(other.messageList)) return false
        } else if (other.messageList != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = unReadMsgNum
        result = 31 * result + (messageList?.contentHashCode() ?: 0)
        return result
    }
}
