package com.luisdev.antsimulator.data.service

import com.luisdev.antsimulator.data.HomeResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.luisdev.antsimulator.core.utils.URL_SERVER

class HomeService(
    private val client: HttpClient
) {
    suspend fun fetchHome(): HomeResult {
        return try {
            val response = client.get("${URL_SERVER}licences/by_version/1") {
                contentType(ContentType.Application.Json)
            }
            HomeResult.Success(response.body())
        } catch (e: Exception) {
            HomeResult.Error("Error al cargar datos: ${e.message}")
        }
    }
}