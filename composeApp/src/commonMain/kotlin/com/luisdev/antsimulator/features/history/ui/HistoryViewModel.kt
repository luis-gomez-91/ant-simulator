package com.luisdev.antsimulator.features.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisdev.antsimulator.features.history.data.HistoryRepository
import com.luisdev.antsimulator.features.history.domain.HistoryEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.luisdev.antsimulator.core.utils.MainViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class HistoryViewModel(
    val mainViewModel: MainViewModel,
    private val testHistoryRepository: HistoryRepository
) : ViewModel() {

    private val _testHistory = MutableStateFlow<List<HistoryEntry>>(emptyList())
    val testHistory: StateFlow<List<HistoryEntry>> = _testHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        mainViewModel.setTitle("Historial de Tests")
//        loadTestHistory()
    }

    fun loadTestHistory(licenceId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            testHistoryRepository.getSimulationsByLicence(licenceId)
                .onEach { historyList ->
                    _testHistory.value = historyList
                    _isLoading.value = false
                }
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                }
                .launchIn(this)
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun formatDate(timestamp: Long): String {
        val dateTime = Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dateTime.dayOfMonth.toString().padStart(2, '0')}/${dateTime.monthNumber.toString().padStart(2, '0')}/${dateTime.year}"
    }

    fun formatTime(timestamp: Long): String {
        val dateTime = Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
    }

    fun formatScore(score: Int, totalQuestions: Int): String {
        return "$score/$totalQuestions"
    }
}

