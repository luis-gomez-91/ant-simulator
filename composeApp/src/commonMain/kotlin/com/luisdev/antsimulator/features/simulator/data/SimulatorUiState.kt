package com.luisdev.antsimulator.features.simulator.data

import com.luisdev.antsimulator.features.question_bank.data.QuestionBankResponse
import org.itb.nominas.core.data.response.ErrorResponse

data class SimulatorUiState(
    val questions: List<QuestionBankResponse> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val userAnswers: Map<Int, Int> = emptyMap(), // Key: questionId, Value: choiceId
    val isLoading: Boolean = false,
    val error: ErrorResponse? = null,

    // --- CAMPOS AÑADIDOS ---
    /**
     * Se vuelve `true` cuando todas las preguntas han sido respondidas.
     * La UI lo usará para mostrar el diálogo de resultados.
     */
    val isFinished: Boolean = false,

    /**
     * Almacena el puntaje final una vez que la simulación ha terminado.
     */
    val score: Int = 0
) {
    // --- GETTERS (sin cambios) ---
    val currentQuestion: QuestionBankResponse?
        get() = questions.getOrNull(currentQuestionIndex)

    val selectedChoiceIdForCurrentQuestion: Int?
        get() = currentQuestion?.id?.let { questionId -> userAnswers[questionId] }

    val allQuestionsAnswered: Boolean
        get() = questions.isNotEmpty() && userAnswers.size == questions.size
}
