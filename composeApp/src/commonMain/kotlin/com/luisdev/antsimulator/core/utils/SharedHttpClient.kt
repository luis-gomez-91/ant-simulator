package com.luisdev.antsimulator.core.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


fun provideHttpClient(): HttpClient = HttpClient {

    install(HttpCookies) {
        storage = AcceptAllCookiesStorage()
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000     // 30 segundos
        connectTimeoutMillis = 15_000     // 15 segundos
        socketTimeoutMillis = 15_000      // 15 segundos
    }

    install(ResponseObserver) {
        onResponse { response ->
            println("Respuesta del servidor: ${response.status}")
            response.headers.forEach { key, values ->
                println("Header: $key = $values")
            }
        }
    }

//    install(Logging) {
//        logger = object : Logger {
//            override fun log(message: String) {
//                Napier.d(message, tag = "KtorLogging")
//            }
//        }
//    }
}
