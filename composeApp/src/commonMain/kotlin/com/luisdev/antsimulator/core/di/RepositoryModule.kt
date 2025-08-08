package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.features.question_bank.data.QuestionBankRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        QuestionBankRepository(
            service = get(),
            database = get()
        )
    }
}