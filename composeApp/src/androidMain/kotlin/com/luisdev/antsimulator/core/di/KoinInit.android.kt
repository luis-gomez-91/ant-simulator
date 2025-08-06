package com.luisdev.antsimulator.core.di

import org.itb.nominas.core.platform.URLOpener
import org.itb.nominas.core.platform.URLOpenerAndroid
import org.koin.dsl.module


actual val nativeModule = module {
    single<URLOpener> { URLOpenerAndroid(get()) }
}