package com.luisdev.antsimulator.features.simulator.ui

// Asegúrate de tener los imports correctos.
// Si usas KMM, reemplaza ViewModel y viewModelScope con los de MOKO MVVM.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisdev.antsimulator.features.simulator.data.SimulatorRepository
import com.luisdev.antsimulator.features.simulator.data.SimulatorUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.itb.nominas.core.data.response.ErrorResponse
import com.luisdev.antsimulator.core.utils.MainViewModel

/**
 * Eventos de navegación de un solo uso que el ViewModel puede enviar a la UI.
 */
sealed class NavigationEvent {
    /**
     * Indica a la UI que debe navegar a la pantalla de resultados.
     * @param simulationId El ID de la simulación guardada para que la siguiente pantalla pueda cargarla.
     */
    data class ToResults(val simulationId: Long) : NavigationEvent()
}

/**
 * ViewModel para la pantalla del simulador. Orquesta el estado de la UI,
 * maneja la lógica de negocio y las interacciones del usuario.
 */
class SimulatorViewModel(
    val mainViewModel: MainViewModel,
    private val repository: SimulatorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SimulatorUiState())
    val uiState: StateFlow<SimulatorUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    /**
     * Carga una nueva serie de 20 preguntas aleatorias para iniciar una simulación.
     */
    fun loadQuestions(licenceId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isFinished = false)
            try {
                val simulatorQuestions = repository.getQuestions(licenceId)
                    ?.shuffled()
                    ?.take(20)
                    ?: emptyList()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    questions = simulatorQuestions,
                    currentQuestionIndex = 0,
                    userAnswers = emptyMap(),
                    score = 0
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message?.let { ErrorResponse(message = it) }
                )
            }
        }
    }

    /**
     * Cambia la pregunta visible cuando el usuario selecciona un número del navegador.
     */
    fun selectQuestionByIndex(index: Int) {
        _uiState.value = _uiState.value.copy(currentQuestionIndex = index)
    }

    /**
     * Registra la respuesta de un usuario para la pregunta actual.
     */
    fun submitAnswer(choiceId: Int) {
        val currentState = _uiState.value
        val currentQuestion = currentState.currentQuestion ?: return

        // No permite cambiar la respuesta una vez seleccionada
        if (currentState.userAnswers.containsKey(currentQuestion.id)) return

        val newAnswers = currentState.userAnswers.toMutableMap()
        newAnswers[currentQuestion.id] = choiceId
        _uiState.value = currentState.copy(userAnswers = newAnswers)

        // ¡LÍNEA ELIMINADA!
        // Ya no llamamos a checkIfAllQuestionsAnswered() desde aquí.
        // La finalización ahora es 100% manual.
    }

    /**
     * Limpia el estado de error, por ejemplo, cuando el usuario cierra un diálogo de alerta.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Inicia el proceso de finalización cuando el usuario presiona el botón "Finalizar".
     * Esta es la ÚNICA manera de finalizar la simulación ahora.
     */
    fun finalizeSimulationManually() {
        // Evita que esta lógica se ejecute varias veces si se llama rápidamente.
        if (_uiState.value.isFinished) return

        viewModelScope.launch {
            // Marca el estado como "finalizado" inmediatamente para prevenir más interacciones.
            _uiState.value = _uiState.value.copy(isFinished = true)

            val currentState = _uiState.value
            var finalScore = 0

            // Prepara los datos para guardar en el repositorio.
            val answersToSave = currentState.questions.mapNotNull { question ->
                currentState.userAnswers[question.id]?.let { userAnswerId ->
                    if (userAnswerId == question.choices.firstOrNull { it.is_correct }?.id) {
                        finalScore++
                    }
                    question to userAnswerId
                }
            }

            // Guarda el intento y obtiene el ID de la simulación guardada.
            mainViewModel.licenceSelected.value?.let {
            val savedSimulationId =
                repository.saveSimulationAttempt(
                    score = finalScore,
                    totalQuestions = currentState.questions.size,
                    answers = answersToSave,
                    licenceId = it.id
                )

                _uiState.value = _uiState.value.copy(score = finalScore)
                _navigationEvent.send(NavigationEvent.ToResults(savedSimulationId))
            }
        }
    }
}
