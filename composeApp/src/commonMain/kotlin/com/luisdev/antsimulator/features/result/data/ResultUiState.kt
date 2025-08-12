package com.luisdev.antsimulator.features.result.data

import com.luisdev.antsimulator.database.Simulation
import com.luisdev.antsimulator.database.SimulationAnswer

data class ResultUiState(
    val simulation: Simulation? = null,
    val answers: List<SimulationAnswer> = emptyList(),
    val isLoading: Boolean = true
)