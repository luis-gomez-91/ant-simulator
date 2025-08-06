package org.itb.nominas.core.utils

import com.luisdev.antsimulator.core.utils.Theme
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
        val theme = Theme.entries.firstOrNull { it.name == value } ?: Theme.SystemDefault
        return theme
    }

}