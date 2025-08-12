package com.luisdev.antsimulator.data.service

import com.luisdev.antsimulator.data.QuestionBankResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.luisdev.antsimulator.core.utils.URL_SERVER

class QuestiosBankService(
    private val client: HttpClient
) {
    suspend fun fetchQuestions(id: Int): QuestionBankResult {
        return try {
            val response = client.get("${URL_SERVER}questions/by_licence/$id") {
                contentType(ContentType.Application.Json)
            }
            QuestionBankResult.Success(response.body())
        } catch (e: Exception) {
            QuestionBankResult.Error("Error al cargar datos: ${e.message}")
        }
    }
}