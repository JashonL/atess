package com.growatt.lib.service

import androidx.annotation.StringDef

@StringDef(ServiceType.HTTP, ServiceType.STORAGE)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ServiceType {
    companion object {
        const val HTTP = "http"
        const val STORAGE = "storage"
    }
}
