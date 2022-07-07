package com.growatt.atess.model.plant

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

data class PlantFilterModel(val filterName: String, val code: Int) {

    companion object {

        fun createFilters(): Array<PlantFilterModel> {
            return arrayOf(
                PlantFilterModel(
                    MainApplication.instance().getString(R.string.install_date), 0
                ), PlantFilterModel(
                    MainApplication.instance().getString(R.string.device_count), 1
                ), PlantFilterModel(
                    MainApplication.instance().getString(R.string.total_component_power), 2
                ), PlantFilterModel(
                    MainApplication.instance().getString(R.string.current_power), 3
                ), PlantFilterModel(
                    MainApplication.instance().getString(R.string.today_power), 4
                ), PlantFilterModel(
                    MainApplication.instance().getString(R.string.total_power), 5
                )
            )
        }

        fun getDefaultFilter(): PlantFilterModel {
            return PlantFilterModel(
                MainApplication.instance().getString(R.string.install_date), 0
            )
        }

    }

    override fun equals(other: Any?): Boolean {
        if (other is PlantFilterModel) {
            if (other.code == code) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        var result = filterName.hashCode()
        result = 31 * result + code
        return result
    }

}