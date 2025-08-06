package com.luisdev.antsimulator.data.service

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType


class MainService (
    private val client: HttpClient
) {
//    suspend fun fetchLastVersion(): LastVersionReponse {
//        val unauthClient = provideUnauthenticatedHttpClient()
//        val response = unauthClient.post("${URL_SERVER}last_version/") {
//            contentType(ContentType.Application.Json)
//        }
//        val raw = response.bodyAsText()
//        Napier.i("Respuesta cruda del servidor: $raw", tag = "lastVersion")
//        return response.body<LastVersionReponse>()
//    }

}