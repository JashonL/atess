package com.growatt.atess.model.plant

/**
 * 电站列表后台返回的数据模型
 */
data class PcsHpsSNModel(
    val pcs: Array<String>,
    val hps: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PcsHpsSNModel

        if (!pcs.contentEquals(other.pcs)) return false
        if (!hps.contentEquals(other.hps)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pcs.contentHashCode()
        result = 31 * result + hps.contentHashCode()
        return result
    }

}
