package com.luisdev.antsimulator.features.question_bank.data
import kotlinx.serialization.Serializable

@Serializable
data class QuestionTypeResponse(
    val id: Int,
    val name: String
)