package com.luisdev.antsimulator.features.question_bank.data

import kotlinx.serialization.Serializable

@Serializable
data class QuestionBankResponse(
    val choices: List<ChoiceResponse>,
    val id: Int,
    val image: String?,
    val licence_type_id: Int,
    val num: Int,
    val question_type: QuestionTypeResponse,
    val question_type_id: Int,
    val text: String,
    var isFavorite: Boolean = false
)