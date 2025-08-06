package com.luisdev.antsimulator.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisdev.antsimulator.data.HomeResult
import com.luisdev.antsimulator.data.service.HomeService
import com.luisdev.antsimulator.features.home.data.LicenceResponse
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.nominas.core.data.response.ErrorResponse
import org.itb.nominas.core.utils.MainViewModel

class HomeViewModel(
    val mainViewModel: MainViewModel,
    val service: HomeService
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<ErrorResponse?>(null)
    val error: StateFlow<ErrorResponse?> = _error

    private val _data = MutableStateFlow<List<LicenceResponse>?>(null)
    val data: StateFlow<List<LicenceResponse>?> = _data

    fun clearError() {
        _error.value = null
    }

    fun clearData() {
        _data.value = null
    }

    fun loadHome() {
        viewModelScope.launch {
            _isLoading.value = true

            val result = service.fetchHome()
            Napier.i("$result", tag = "homeViewmodel")

            when (result) {
                is HomeResult.Success -> {
                    _data.value = result.data
                    _error.value = null
                }
                is HomeResult.Error -> {
                    _error.value = ErrorResponse(code = "error", message = result.message)
                }
            }
            _isLoading.value = false
        }
    }
}