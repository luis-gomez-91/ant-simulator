package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.core.platform.DatabaseDriverFactory
import com.luisdev.antsimulator.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        // Koin buscará en sus módulos (incluido 'nativeModule')
        // para encontrar cómo satisfacer este 'get<DatabaseDriverFactory>()'.
        AppDatabase(driver = get<DatabaseDriverFactory>().createDriver())
    }
}