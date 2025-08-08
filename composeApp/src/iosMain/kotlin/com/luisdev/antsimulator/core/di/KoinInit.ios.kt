package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.core.platform.DatabaseDriverFactory
import org.itb.nominas.core.platform.URLOpener
import org.itb.nominas.core.platform.URLOpenerIOS
import org.koin.core.module.Module
import org.koin.dsl.module

actual val nativeModule: Module = module {
    single<URLOpener> { URLOpenerIOS() }
    single<DatabaseDriverFactory> { DatabaseDriverFactory() }
}