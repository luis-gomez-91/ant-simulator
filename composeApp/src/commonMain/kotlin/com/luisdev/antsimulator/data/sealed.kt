package com.luisdev.antsimulator.data

import com.luisdev.antsimulator.features.home.data.LicenceResponse

sealed class HomeResult {
    data class Success(val data: List<LicenceResponse>) : HomeResult()
    data class Error(val message: String) : HomeResult()
}