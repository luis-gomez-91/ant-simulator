package com.luisdev.antsimulator.core.utils


fun String.unaccent(): String {
    val original = "áéíóúÁÉÍÓÚ"
    val normalized = "aeiouAEIOU"

    // Crea un mapa de caracteres para un reemplazo eficiente
    val charMap = original.zip(normalized).toMap()

    // Construye la nueva cadena reemplazando los caracteres
    return this.map { charMap[it] ?: it }.joinToString("")
}
