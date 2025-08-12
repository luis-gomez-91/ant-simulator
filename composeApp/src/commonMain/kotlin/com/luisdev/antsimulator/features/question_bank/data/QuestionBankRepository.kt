package com.luisdev.antsimulator.features.question_bank.data

import com.luisdev.antsimulator.AppDatabase
import com.luisdev.antsimulator.data.QuestionBankResult
import com.luisdev.antsimulator.data.service.QuestiosBankService


class QuestionBankRepository(
    private val service: QuestiosBankService,
    private val database: AppDatabase
) {
    private val favoriteQueries = database.favoritesQueries

    suspend fun getQuestions(
        licenceId: Int,
        onFavorite: Boolean
    ): List<QuestionBankResponse> {
        val apiResult = service.fetchQuestions(licenceId)

        if (apiResult is QuestionBankResult.Success) {
            val questionsFromApi = apiResult.data

            if (onFavorite) {
                val favoriteIds = favoriteQueries.selectAll().executeAsList().toSet()
                return questionsFromApi.map { question ->
                    question.copy(isFavorite = question.id.toLong() in favoriteIds)
                }
            }
            return questionsFromApi

        } else {
            return emptyList()
        }
    }

    suspend fun addFavorite(questionId: Int) {
        favoriteQueries.insert(question_id = questionId.toLong())
    }

    suspend fun removeFavorite(questionId: Int) {
        favoriteQueries.delete(question_id = questionId.toLong())
    }
}
