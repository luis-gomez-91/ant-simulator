package com.luisdev.antsimulator.features.result.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.luisdev.antsimulator.core.ui.components.MyCard
import com.luisdev.antsimulator.core.ui.components.MyFilledTonalButton
import com.luisdev.antsimulator.database.SimulationAnswer
import com.luisdev.antsimulator.features.question_bank.data.ChoiceResponse
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Checkmark
import compose.icons.evaicons.outline.Close
import kotlinx.serialization.json.Json
import org.itb.nominas.core.navigation.HomeRoute
import org.itb.nominas.core.navigation.SimulatorRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ResultScreen(
    simulationId: Long,
    navHostController: NavHostController,
    resultViewModel: ResultViewModel = koinViewModel { parametersOf(simulationId) }
) {
    val uiState by resultViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomBar(
                onNavigateHome = {
                    navHostController.navigate(HomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }, onNavigateSimulator = {
                    navHostController.navigate(SimulatorRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            val simulation = uiState.simulation
            if (simulation == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron resultados para esta simulación.")
                }
                return@Column
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Cabecera con el puntaje ---
                item {
                    ScoreHeader(score = simulation.score, totalQuestions = simulation.total_questions)
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "Detalle de respuestas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // --- Lista de respuestas ---
                items(uiState.answers) { answer ->
                    AnswerDetailCard(answer = answer)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    onNavigateHome: () -> Unit,
    onNavigateSimulator: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            MyFilledTonalButton(
                text = "Inicio",
                onClickAction = onNavigateHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            MyFilledTonalButton(
                text = "Reiniciar",
                onClickAction = onNavigateSimulator,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

    }
}

@Composable
private fun ScoreHeader(score: Long, totalQuestions: Long) {
    val isApproved = score >= 17

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (isApproved) "¡Aprobado!" else "Reprobado",
            style = MaterialTheme.typography.headlineMedium,
            color = if (isApproved) Color(0xFF388E3C) else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tu puntaje fue:",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "$score / $totalQuestions",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun AnswerDetailCard(answer: SimulationAnswer) {
    val choices = Json.decodeFromString<List<ChoiceResponse>>(answer.choices_json)
    val userChoiceText = choices.find { it.id.toLong() == answer.user_choice_id }?.text ?: "No encontrada"
    val correctChoiceText = choices.find { it.id.toLong() == answer.correct_choice_id }?.text ?: "No encontrada"
    val wasCorrect = answer.user_choice_id == answer.correct_choice_id

    MyCard {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = answer.question_text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = if (wasCorrect) EvaIcons.Outline.Checkmark else EvaIcons.Outline.Close,
                    contentDescription = if (wasCorrect) "Correcto" else "Incorrecto",
                    tint = if (wasCorrect) Color(0xFF388E3C) else MaterialTheme.colorScheme.error
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Tu respuesta: $userChoiceText",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!wasCorrect) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Respuesta correcta: $correctChoiceText",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}
