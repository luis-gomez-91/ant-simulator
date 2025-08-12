package com.luisdev.antsimulator.core.utils

const val URL_SERVER = "https://ant-simulator-back-production.up.railway.app/"
const val KEY_THEME = "theme"
const val KEY_FONT_SIZE = "font_size"

enum class FontSizeOption(val scale: Float) {
    Pequeña(0.85f),
    Mediana(1.0f),
    Grande(1.15f),
    ExtraGrande(1.3f);

    companion object {
        fun fromName(name: String?): FontSizeOption {
            return entries.firstOrNull { it.name == name } ?: Mediana
        }
    }
}