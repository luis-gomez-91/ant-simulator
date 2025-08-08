package com.luisdev.antsimulator.domain

import androidx.compose.ui.graphics.vector.ImageVector

data class OptionButtomItem(
    val text: String,
    val icon: ImageVector,
    val onclick: () -> Unit
)
