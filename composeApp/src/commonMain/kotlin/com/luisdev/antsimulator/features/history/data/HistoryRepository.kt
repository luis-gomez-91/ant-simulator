package com.luisdev.antsimulator.features.history.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.luisdev.antsimulator.AppDatabase
import com.luisdev.antsimulator.features.history.domain.HistoryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class HistoryRepository(private val database: AppDatabase) {

    fun getSimulationsByLicence(licenceId: Int): Flow<List<HistoryEntry>> {
        return database.historyQueries
            .selectSimulationsByLicence(licenceId.toLong())
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { simulations ->
                simulations.map {
                    HistoryEntry(
                        id = it.id,
                        date = it.date,
                        score = it.score.toInt(),
                        totalQuestions = it.total_questions.toInt()
                    )
                }
            }
    }


    fun getSimulationById(id: Long): Flow<HistoryEntry?> {
        return database.historyQueries.getSimulationById(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { simulationList ->
                simulationList.firstOrNull()?.let { simulation ->
                    HistoryEntry(
                        id = simulation.id,
                        date = simulation.date,
                        score = simulation.score.toInt(),
                        totalQuestions = simulation.total_questions.toInt()
                    )
                }
            }
    }
}

