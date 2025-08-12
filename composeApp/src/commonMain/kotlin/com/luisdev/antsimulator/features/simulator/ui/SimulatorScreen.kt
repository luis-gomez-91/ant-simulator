package com.luisdev.antsimulator.features.simulator.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.lexilabs.basic.ads.AdState
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.composable.InterstitialAd
import app.lexilabs.basic.ads.composable.rememberInterstitialAd
import coil3.compose.AsyncImage
import com.luisdev.antsimulator.core.ui.components.ErrorAlert
import com.luisdev.antsimulator.core.ui.components.MyCard
import com.luisdev.antsimulator.core.ui.components.MyFilledTonalButton
import com.luisdev.antsimulator.features.question_bank.data.QuestionBankResponse
import com.luisdev.antsimulator.features.question_bank.ui.FullScreenImageTransition
import com.luisdev.antsimulator.features.simulator.data.SimulatorUiState
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ChevronLeft
import compose.icons.evaicons.outline.ChevronRight
import org.itb.nominas.core.components.MainScaffold
import org.itb.nominas.core.navigation.ResultRoute
import org.itb.nominas.core.navigation.SimulatorRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SimulatorScreen(
    navHostController: NavHostController,
    simulatorViewModel: SimulatorViewModel = koinViewModel(),
    activity: Any?
) {
    MainScaffold(
        navController = navHostController,
        mainViewModel = simulatorViewModel.mainViewModel,
        content = {
            Screen(
                simulatorViewModel,
                navHostController,
                activity
            )
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class, DependsOnGoogleMobileAds::class)
@Composable
fun Screen(
    simulatorViewModel: SimulatorViewModel,
    navController: NavHostController,
    activity: Any?
) {
    // Observa el único estado desde el ViewModel
    val uiState by simulatorViewModel.uiState.collectAsState()
    val licence by simulatorViewModel.mainViewModel.licenceSelected.collectAsState(null)
    var selectedQuestion by remember { mutableStateOf<QuestionBankResponse?>(null) }

    val interstitialAd by rememberInterstitialAd(activity)
    var showInterstitialAd by remember { mutableStateOf(false) }
    var navigateAfterAd by remember { mutableStateOf(false) }

    licence?.let {
        simulatorViewModel.mainViewModel.setTitle("Licencia tipo ${it.name}")
        // Usa it.id como key para que se recargue si la licencia cambia
        LaunchedEffect(it.id) {
            simulatorViewModel.loadQuestions(it.id)
        }
    }

    LaunchedEffect(Unit) {
        simulatorViewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.ToResults -> {
                    navController.navigate(ResultRoute(simulationId = event.simulationId)) {
                        popUpTo(SimulatorRoute.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.questions.isNotEmpty()) {
                    item {
                        QuestionNavigator(
                            uiState = uiState,
                            onQuestionSelected = { index -> simulatorViewModel.selectQuestionByIndex(index) }
                        )
                    }
                    item {
                        uiState.currentQuestion?.let { question ->
                            QuestionDetails(
                                question = question,
                                selectedChoiceId = uiState.selectedChoiceIdForCurrentQuestion,
                                onChoiceSelected = { choiceId -> simulatorViewModel.submitAnswer(choiceId) },
                                onImageClick = { selectedQuestion = question }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MyFilledTonalButton(
                    text = "",
                    icon = EvaIcons.Outline.ChevronLeft,
                    enabled = uiState.currentQuestionIndex != 0,
                    onClickAction = { simulatorViewModel.selectQuestionByIndex(uiState.currentQuestionIndex - 1) },
                    iconSize = 28.dp
                )

                MyFilledTonalButton(
                    text = "Finalizar",
//                    enabled = uiState.allQuestionsAnswered && !uiState.isFinished,
//                    onClickAction = {simulatorViewModel.finalizeSimulationManually()},
                    onClickAction = { showInterstitialAd = true },
                    enabled = interstitialAd.state == AdState.READY,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    textStyle = MaterialTheme.typography.titleLarge
                )

                MyFilledTonalButton(
                    text = "",
                    icon = EvaIcons.Outline.ChevronRight,
                    enabled = uiState.currentQuestionIndex != 19,
                    onClickAction = { simulatorViewModel.selectQuestionByIndex(uiState.currentQuestionIndex + 1) },
                    iconSize = 28.dp
                )
            }
        }

        AnimatedVisibility(
            visible = selectedQuestion != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            selectedQuestion?.let { question ->
                FullScreenImageTransition(
                    imageUrl = question.image!!,
                    onDismiss = { selectedQuestion = null },
                )
            }
        }
    }

    uiState.error?.let {
        ErrorAlert(error = it, onDismiss = { simulatorViewModel.clearError() })
    }

    if (navigateAfterAd) {
        LaunchedEffect(Unit) {
            navController.navigate(ResultRoute(simulationId = 1)) {
                popUpTo(SimulatorRoute.route) { inclusive = true }
            }
        }
    }

    if (showInterstitialAd && interstitialAd.state == AdState.READY) {
        InterstitialAd(interstitialAd, onDismissed = {
            showInterstitialAd = false
            navigateAfterAd = true
        })
    }
}

@Composable
private fun QuestionNavigator(
    uiState: SimulatorUiState,
    onQuestionSelected: (Int) -> Unit
) {
    FlowRow(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        uiState.questions.forEachIndexed { index, question ->
            val userAnswerId = uiState.userAnswers[question.id]
            val isAnswered = userAnswerId != null
            val isCorrect = isAnswered && question.choices.find { it.id == userAnswerId }?.is_correct == true

            val backgroundColor = when {
                isAnswered && isCorrect -> MaterialTheme.colorScheme.tertiaryContainer
                isAnswered && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                index == uiState.currentQuestionIndex -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surfaceContainer
            }

            MyCard(
                modifier = Modifier.size(40.dp),
                containerColor = backgroundColor,
                onClick = { onQuestionSelected(index) }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionDetails(
    question: QuestionBankResponse,
    selectedChoiceId: Int?,
    onChoiceSelected: (Int) -> Unit,
    onImageClick: (QuestionBankResponse) -> Unit
) {
    val hasBeenAnswered = selectedChoiceId != null

    Column(modifier = Modifier.padding(16.dp)) {
        // --- Texto de la pregunta e imagen ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "${question.num}. ${question.text}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            question.image?.let {
                Spacer(Modifier.width(8.dp))
                AsyncImage(
                    model = it,
                    contentDescription = "Question image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(80.dp)
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onImageClick(question) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- Opciones de respuesta ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            question.choices.forEach { choice ->
                val isThisChoiceSelected = choice.id == selectedChoiceId

                val backgroundColor = when {
                    isThisChoiceSelected && choice.is_correct -> MaterialTheme.colorScheme.tertiaryContainer
                    isThisChoiceSelected && !choice.is_correct -> MaterialTheme.colorScheme.errorContainer
                    hasBeenAnswered && choice.is_correct -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                    else -> MaterialTheme.colorScheme.surfaceContainer
                }

                val textColor = when {
                    isThisChoiceSelected || (hasBeenAnswered && choice.is_correct) -> MaterialTheme.colorScheme.onTertiaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = !hasBeenAnswered) { onChoiceSelected(choice.id) },
                    color = backgroundColor
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = choice.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
            }
        }
    }
}
