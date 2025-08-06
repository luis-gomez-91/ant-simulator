package com.luisdev.antsimulator.features.home.data

import kotlinx.serialization.Serializable

@Serializable
data class TypeResponse(
    val id: Int,
    val name: String
)