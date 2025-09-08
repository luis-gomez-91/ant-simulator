package com.luisdev.antsimulator.core.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() }
        ) {
            LazyColumn(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp)) {
                item {
                    Text(
                        text = "AVISO LEGAL IMPORTANTE",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Esta aplicación NO está afiliada, patrocinada ni aprobada por la Agencia Nacional de Tránsito (ANT) de Ecuador ni por ninguna otra entidad del gobierno.\n" +
                                "\n" +
                                "Toda la información utilizada se basa en material de acceso público disponible en: https://www.ant.gob.ec/banco-de-preguntas-para-licencias/\n" +
                                "\n" +
                                "El propósito exclusivo de esta aplicación es educativo y de práctica personal para el examen teórico de licencias de conducir.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Text(
//                        text = "Simulador ANT",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = "Es una herramienta diseñada para ofrecer una preparación completa y efectiva para el examen teórico de la ANT en Ecuador. Es ideal para aspirantes a licencias de conducir de distintos tipos y ofrece una experiencia de estudio flexible, integral y fácil de usar.",
//                        style = MaterialTheme.typography.labelSmall,
//                        color = MaterialTheme.colorScheme.secondary
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = "Características principales:",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//
//                val features = listOf(
//                    "Banco de preguntas actualizado regularmente: Accede a una amplia colección de preguntas, basada en la información más reciente.",
//                    "Buscador inteligente: Encuentra cualquier pregunta al instante para un estudio más rápido.",
//                    "Preguntas favoritas: Guarda las preguntas más difíciles para un repaso personalizado.",
//                    "Simulador de examen realista: Practica con simulacros de examen.",
//                    "Historial de intentos: Mide tu progreso y evalúa tu rendimiento con un registro detallado de tus prácticas."
//                )
//
//                items(features) { feature ->
//                    Text(
//                        text = "• $feature",
//                        style = MaterialTheme.typography.labelSmall,
//                        color = MaterialTheme.colorScheme.secondary
//                    )
                }
            }
        }
    }
}