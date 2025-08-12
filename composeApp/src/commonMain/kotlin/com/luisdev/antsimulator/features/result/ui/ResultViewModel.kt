package com.luisdev.antsimulator.features.result.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisdev.antsimulator.features.result.data.ResultUiState
import com.luisdev.antsimulator.features.simulator.data.SimulatorRepository // Necesitarás un método nuevo aquí
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    private val simulationId: Long, // Recibe el ID como parámetro
    private val repository: SimulatorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    init {
        loadResults()
    }

    private fun loadResults() {
        viewModelScope.launch {
            val simulation = repository.getSimulationById(simulationId)
            val answers = repository.getAnswersForSimulation(simulationId)
            _uiState.value = ResultUiState(
                simulation = simulation,
                answers = answers,
                isLoading = false
            )
        }
    }
}
