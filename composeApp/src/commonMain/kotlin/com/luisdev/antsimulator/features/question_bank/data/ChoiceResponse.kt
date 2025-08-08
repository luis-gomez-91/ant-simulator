package com.luisdev.antsimulator.features.question_bank.data
import kotlinx.serialization.Serializable

@Serializable
data class ChoiceResponse(
    val id: Int,
    val image: String?,
    val is_correct: Boolean,
    val question_id: Int,
    val text: String
)