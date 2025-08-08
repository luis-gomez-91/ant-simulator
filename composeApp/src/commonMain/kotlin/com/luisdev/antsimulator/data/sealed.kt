package com.luisdev.antsimulator.data

import com.luisdev.antsimulator.features.home.data.LicenceResponse
import com.luisdev.antsimulator.features.question_bank.data.QuestionBankResponse

sealed class HomeResult {
    data class Success(val data: List<LicenceResponse>) : HomeResult()
    data class Error(val message: String) : HomeResult()
}

sealed class QuestionBankResult {
    data class Success(val data: List<QuestionBankResponse>) : QuestionBankResult()
    data class Error(val message: String) : QuestionBankResult()
}