package com.luisdev.antsimulator.core.platform

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.luisdev.antsimulator.AppDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        // Borra la base de datos anterior (útil para desarrollo, no para producción)
//        context.deleteDatabase("app.db")

        return AndroidSqliteDriver(AppDatabase.Schema, context, "app.db")
    }
}
