package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.features.history.data.HistoryRepository
import com.luisdev.antsimulator.features.question_bank.data.QuestionBankRepository
import com.luisdev.antsimulator.features.simulator.data.SimulatorRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        QuestionBankRepository(
            service = get(),
            database = get()
        )
    }

    single {
        SimulatorRepository(
            service = get(),
            database = get()
        )
    }

    single {
        HistoryRepository(
            database = get()
        )
    }
}