package com.luisdev.antsimulator.core.ui.components

import androidx.compose.runtime.Composable
import org.itb.nominas.core.data.response.ErrorResponse

@Composable
fun ErrorAlert(
    error: ErrorResponse?,
    onDismiss: () -> Unit
) {
    error?.let {
        MyErrorAlert(
            titulo = "Error",
            mensaje = it.message,
            onDismiss = {
                onDismiss()
            },
            showAlert = true
        )
    }
}