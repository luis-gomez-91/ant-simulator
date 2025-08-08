package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.data.service.HomeService
import com.luisdev.antsimulator.data.service.MainService
import com.luisdev.antsimulator.data.service.QuestiosBankService
import org.koin.dsl.module

val serviceModule = module {
    single { MainService(get()) }
    single { HomeService(get()) }
    single { QuestiosBankService(get()) }
}