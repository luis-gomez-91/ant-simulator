package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.features.home.ui.HomeViewModel
import com.luisdev.antsimulator.features.options.OptionsViewModel
import com.luisdev.antsimulator.features.simulator.ui.SimulatorViewModel
import com.luisdev.antsimulator.features.question_bank.ui.QuestionBankViewModel
import com.luisdev.antsimulator.features.history.ui.HistoryViewModel
import com.luisdev.antsimulator.features.result.ui.ResultViewModel
import com.luisdev.antsimulator.core.utils.MainViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    single { MainViewModel(get()) }
    viewModelOf(::HomeViewModel)
    viewModelOf(::OptionsViewModel)
    viewModelOf(::QuestionBankViewModel)
    viewModelOf(::SimulatorViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::HistoryViewModel)

}