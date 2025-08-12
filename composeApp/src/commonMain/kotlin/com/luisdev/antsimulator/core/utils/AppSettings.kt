package com.luisdev.antsimulator.core.utils

import org.itb.nominas.core.platform.getSettings

object AppSettings {
    private val settings by lazy {
        getSettings()
    }

    fun setTheme(theme: Theme) {
        settings.putString(KEY_THEME, theme.name)
    }

    fun getTheme(): Theme {
        val value = settings.getString(KEY_THEME, defaultValue = Theme.SystemDefault.name)
        val theme = Theme.entries.firstOrNull { it.name == value } ?: Theme.Light
        return theme
    }

    fun setFontSize(option: FontSizeOption) {
        settings.putString(KEY_FONT_SIZE, option.name)
    }

    fun getFontSize(): FontSizeOption {
        val value = settings.getString(KEY_FONT_SIZE, defaultValue = FontSizeOption.Mediana.name)
        return FontSizeOption.fromName(value)
    }
}