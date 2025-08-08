package org.itb.nominas.core.utils

import androidx.lifecycle.ViewModel
import com.luisdev.antsimulator.core.utils.Theme
import com.luisdev.antsimulator.data.service.MainService
import com.luisdev.antsimulator.features.home.data.LicenceResponse
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.itb.nominas.core.data.response.ErrorResponse
import org.itb.nominas.core.platform.URLOpener


class MainViewModel(
    val service: MainService,
    val urlOpener: URLOpener,
): ViewModel() {

    private val _title = MutableStateFlow<String?>(null)
    val title: StateFlow<String?> = _title

    private val _selectedTheme = MutableStateFlow<Theme>(AppSettings.getTheme())
    val selectedTheme: StateFlow<Theme> = _selectedTheme

    private val _error = MutableStateFlow<ErrorResponse?>(null)
    val error: StateFlow<ErrorResponse?> = _error

    private val _bottomSheetTheme = MutableStateFlow<Boolean>(false)
    val bottomSheetTheme: StateFlow<Boolean> = _bottomSheetTheme

    private val _licenceSelected = MutableStateFlow<LicenceResponse?>(null)
    val licenceSelected: StateFlow<LicenceResponse?> = _licenceSelected

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isStarred = MutableStateFlow<Boolean>(false)
    val isStarred: StateFlow<Boolean> = _isStarred

    fun setIsStarred(value: Boolean) {
        _isStarred.value = value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setLicenteSelected(licence: LicenceResponse?) {
        _licenceSelected.value = licence
    }

    fun setTheme(newValue: Theme) {
        _selectedTheme.value = newValue
        AppSettings.setTheme(newValue)
    }

    fun setBottomSheetTheme(newValue: Boolean) {
        _bottomSheetTheme.value = newValue
    }

    fun setTitle(newValue: String?) {
        _title.value = newValue
    }

    fun setError(newValue: String) {
        _error.value = ErrorResponse(code = "error", message = newValue)
    }

    fun clearError() { _error.value = null }
}