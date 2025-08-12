package com.luisdev.antsimulator.features.options

import androidx.lifecycle.ViewModel
import com.luisdev.antsimulator.features.home.data.LicenceResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.itb.nominas.core.data.response.ErrorResponse
import com.luisdev.antsimulator.core.utils.MainViewModel


class OptionsViewModel(
    val mainViewModel: MainViewModel
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<ErrorResponse?>(null)
    val error: StateFlow<ErrorResponse?> = _error

    private val _data = MutableStateFlow<List<LicenceResponse>?>(null)
    val data: StateFlow<List<LicenceResponse>?> = _data
}