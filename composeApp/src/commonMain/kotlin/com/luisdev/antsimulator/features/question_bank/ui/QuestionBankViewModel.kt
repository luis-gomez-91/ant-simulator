package com.luisdev.antsimulator.features.question_bank.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisdev.antsimulator.features.question_bank.data.QuestionBankRepository
import com.luisdev.antsimulator.features.question_bank.data.QuestionBankResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.nominas.core.data.response.ErrorResponse
import org.itb.nominas.core.utils.MainViewModel


class QuestionBankViewModel(
    val mainViewModel: MainViewModel,
    private val repository: QuestionBankRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<ErrorResponse?>(null)
    val error: StateFlow<ErrorResponse?> = _error

    private val _data = MutableStateFlow<List<QuestionBankResponse>?>(null)
    val data: StateFlow<List<QuestionBankResponse>?> = _data

    fun clearError() {
        _error.value = null
    }

    fun clearData() {
        _data.value = null
    }

    fun loadQuestionBank(licenceId: Int, onFavorite: Boolean = true) {
        viewModelScope.launch {
            _data.value = repository.getQuestions(licenceId, onFavorite)
        }
    }

    fun toggleFavorite(questionToToggle: QuestionBankResponse) {
        viewModelScope.launch {
            // Llama al repositorio para que guarde el cambio en la BD
            if (questionToToggle.isFavorite) {
                repository.removeFavorite(questionToToggle.id)
            } else {
                repository.addFavorite(questionToToggle.id)
            }

            // Actualiza la UI inmediatamente para una respuesta visual rápida
            _data.value = _data.value?.map { currentQuestion ->
                if (currentQuestion.id == questionToToggle.id) {
                    currentQuestion.copy(isFavorite = !currentQuestion.isFavorite)
                } else {
                    currentQuestion
                }
            }
        }
    }
}