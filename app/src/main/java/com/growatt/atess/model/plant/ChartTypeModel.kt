package com.growatt.atess.model.plant

/**
 * 图表类型
 */
data class ChartTypeModel(
    val type: String,//图表类型，对应上传给服务端字段
    val typeName: String,//类型名称
    val typeUnit: String//类型单位
) {
    companion object {
        fun getTypeNames(chartTypes: Array<ChartTypeModel>): Array<String> {
            return Array(chartTypes.size) { index -> chartTypes[index].typeName }
        }
    }
}