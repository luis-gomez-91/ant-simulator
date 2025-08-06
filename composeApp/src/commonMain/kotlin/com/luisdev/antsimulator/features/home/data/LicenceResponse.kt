package com.luisdev.antsimulator.features.home.data

import kotlinx.serialization.Serializable

@Serializable
data class LicenceResponse(
    val description: String,
    val enable: Boolean,
    val id: Int,
    val image: String,
    val name: String,
    val order: Int?,
    val question_bank: String,
    val type: TypeResponse,
    val type_id: Int,
    val version: VersionResponse,
    val version_id: Int
)