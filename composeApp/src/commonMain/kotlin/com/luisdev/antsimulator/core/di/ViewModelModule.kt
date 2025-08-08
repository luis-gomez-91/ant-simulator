package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.features.home.ui.HomeViewModel
import com.luisdev.antsimulator.features.options.OptionsViewModel
import com.luisdev.antsimulator.features.question_bank.ui.QuestionBankViewModel
import org.itb.nominas.core.utils.MainViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    single { MainViewModel(get(), get()) }
    viewModelOf(::HomeViewModel)
    viewModelOf(::OptionsViewModel)
    viewModelOf(::QuestionBankViewModel)
}