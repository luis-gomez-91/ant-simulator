package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.data.service.HomeService
import com.luisdev.antsimulator.data.service.MainService
import org.koin.dsl.module

val serviceModule = module {
    single { MainService(get()) }
    single { HomeService(get()) }
}