package com.luisdev.antsimulator.features.simulator.data

import com.luisdev.antsimulator.AppDatabase
import com.luisdev.antsimulator.features.question_bank.data.QuestionBankResponse
import com.luisdev.antsimulator.data.QuestionBankResult
import com.luisdev.antsimulator.data.service.QuestiosBankService
import com.luisdev.antsimulator.database.Simulation
import com.luisdev.antsimulator.database.SimulationAnswer
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class SimulatorRepository(
    private val service: QuestiosBankService,
    private val database: AppDatabase
) {
    suspend fun getQuestions(
        licenceId: Int,
    ): List<QuestionBankResponse> {
        val apiResult = service.fetchQuestions(licenceId)

        return if (apiResult is QuestionBankResult.Success) {
            apiResult.data
        } else {
            emptyList()
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun saveSimulationAttempt(
        licenceId: Int,
        score: Int,
        totalQuestions: Int,
        answers: List<Pair<QuestionBankResponse, Int>> // Par de (Pregunta, ID de respuesta del usuario)
    ) : Long {
        val simulationDate = Clock.System.now().toEpochMilliseconds()
        var simulationId: Long = -1
        // Usamos una transacción para asegurar que todo se guarde como una unidad atómica.
        // Si algo falla, se revierte todo.
        database.historyQueries.transaction {
            // 1. Inserta la simulación. Esta llamada ya no devuelve nada.
            database.historyQueries.insertSimulation(
                date = simulationDate,
                score = score.toLong(),
                total_questions = totalQuestions.toLong(),
                licence_id = licenceId.toLong()
            )

            // 2. OBTÉN EL ID DE LA FILA RECIÉN INSERTADA.
            // Esta es la query que añadimos para reemplazar a RETURNING.
            // .executeAsOne() aquí sí es correcto, porque `last_insert_rowid()` devuelve un único valor.
            simulationId = database.historyQueries.lastInsertRowId().executeAsOne()

            // 3. Itera sobre cada respuesta y guárdala con el ID de la simulación
            answers.forEach { (question, userAnswerId) ->
                val correctChoiceId = question.choices.first { it.is_correct }.id
                // Serializa la lista de opciones a un string JSON
                val choicesJson = Json.encodeToString(question.choices)

                database.historyQueries.insertAnswer(
                    simulation_id = simulationId,
                    question_text = question.text,
                    choices_json = choicesJson,
                    user_choice_id = userAnswerId.toLong(),
                    correct_choice_id = correctChoiceId.toLong()
                )
            }
        }
        return simulationId
    }

    suspend fun getSimulationById(id: Long): Simulation? {
        return database.historyQueries.getSimulationById(id).executeAsOneOrNull()
    }

    suspend fun getAnswersForSimulation(simulationId: Long): List<SimulationAnswer> {
        return database.historyQueries.getAnswersForSimulation(simulationId).executeAsList()
    }
}
