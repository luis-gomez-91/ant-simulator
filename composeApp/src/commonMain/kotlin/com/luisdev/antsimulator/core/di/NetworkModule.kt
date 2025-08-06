package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.core.utils.provideHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { provideHttpClient() }
}