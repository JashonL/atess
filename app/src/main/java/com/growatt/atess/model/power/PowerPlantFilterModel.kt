package com.growatt.atess.model.power

import com.growatt.atess.R
import com.growatt.atess.application.MainApplication

data class PowerPlantFilterModel(val filterName: String, val code: Int) {

    companion object {

        fun createFilters(): Array<PowerPlantFilterModel> {
            return arrayOf(
                PowerPlantFilterModel(
                    MainApplication.instance().getString(R.string.install_date), 0
                ), PowerPlantFilterModel(
                    MainApplication.instance().getString(R.string.device_count), 1
                ), PowerPlantFilterModel(
                    MainApplication.instance().getString(R.string.total_component_power), 2
                ), PowerPlantFilterModel(
                    MainApplication.instance().getString(R.string.current_power), 3
                ), PowerPlantFilterModel(
                    MainApplication.instance().getString(R.string.today_power), 4
                ), PowerPlantFilterModel(
                    MainApplication.instance().getString(R.string.total_power), 5
                )
            )
        }

        fun getDefaultFilter(): PowerPlantFilterModel {
            return PowerPlantFilterModel(
                MainApplication.instance().getString(R.string.install_date), 0
            )
        }

    }

    override fun equals(other: Any?): Boolean {
        if (other is PowerPlantFilterModel) {
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