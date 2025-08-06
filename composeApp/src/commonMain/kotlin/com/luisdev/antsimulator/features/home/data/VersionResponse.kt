package com.luisdev.antsimulator.features.home.data

import kotlinx.serialization.Serializable

@Serializable
data class VersionResponse(
    val enable: Boolean,
    val id: Int,
    val year: Int
)