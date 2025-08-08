package com.luisdev.antsimulator.core.di

import com.luisdev.antsimulator.core.platform.DatabaseDriverFactory
import org.itb.nominas.core.platform.URLOpener
import org.itb.nominas.core.platform.URLOpenerAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


actual val nativeModule = module {
    single<URLOpener> { URLOpenerAndroid(get()) }
    single<DatabaseDriverFactory> { DatabaseDriverFactory(androidContext()) }
}