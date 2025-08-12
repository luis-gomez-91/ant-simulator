package com.luisdev.antsimulator.features.question_bank.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.composable.BannerAd
import coil3.compose.AsyncImage
import com.luisdev.antsimulator.core.ui.components.ErrorAlert
import com.luisdev.antsimulator.core.ui.components.ShimmerLoadingAnimation
import com.luisdev.antsimulator.core.utils.unaccent
import com.luisdev.antsimulator.features.question_bank.data.QuestionBankResponse
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Star
import compose.icons.evaicons.outline.Star
import org.itb.nominas.core.components.MainScaffold
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
fun QuestionBankScreen(
    navHostController: NavHostController,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    quesionBankViewModel: QuestionBankViewModel = koinViewModel()
) {
    MainScaffold(
        navController = navHostController,
        mainViewModel = quesionBankViewModel.mainViewModel,
        content = {
            Screen(
                quesionBankViewModel,
                animatedContentScope,
                sharedTransitionScope
            )
        }
    )
}

@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class,
    DependsOnGoogleMobileAds::class
)
@Composable
fun Screen(
    quesionBankViewModel: QuestionBankViewModel,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val data by quesionBankViewModel.data.collectAsState(null)
    val error by quesionBankViewModel.error.collectAsState(null)
    val isLoading by quesionBankViewModel.isLoading.collectAsState(false)
    val licence by quesionBankViewModel.mainViewModel.licenceSelected.collectAsState(null)
    var selectedQuestion by remember { mutableStateOf<QuestionBankResponse?>(null) }
    val query by quesionBankViewModel.mainViewModel.searchQuery.collectAsState("")
    val isStarred by quesionBankViewModel.mainViewModel.isStarred.collectAsState(false)

    licence?.let {
        quesionBankViewModel.mainViewModel.setTitle("Licencia tipo ${it.name}")
        LaunchedEffect(Unit) {
            quesionBankViewModel.loadQuestionBank(it.id)
            quesionBankViewModel.mainViewModel.setSearchQuery("")
        }
    }

    val filteredQuestions = remember(data, query, isStarred) {
        data
            ?.sortedBy { it.num }
            ?.filter { question ->
                // Condición 1: Filtro de favoritos
                val isFavoriteMatch = if (isStarred) question.isFavorite else true

                // Condición 2: Filtro de búsqueda
                val isQueryMatch = if (query.isBlank()) {
                    true
                } else {
                    question.text.unaccent().contains(query.unaccent(), ignoreCase = true)
                }

                // La pregunta debe cumplir AMBAS condiciones
                isFavoriteMatch && isQueryMatch
            }
    }

    if (isLoading) {
        Column (
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ShimmerLoadingAnimation(3)
        }
    } else {
        filteredQuestions?.let { questions ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(questions) { question ->
                            QuestionItem(
                                questionBankViewModel = quesionBankViewModel,
                                question = question,
                                onImageClick = { selectedQuestion = question },
                                animatedContentScope = animatedContentScope,
                                sharedTransitionScope = sharedTransitionScope
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = selectedQuestion != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        // El ?.let previene el crash durante la animación de salida.
                        selectedQuestion?.let { question ->
                            FullScreenImageTransition(
                                imageUrl = question.image!!,
                                onDismiss = { selectedQuestion = null },
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    BannerAd()
                }
            }
        }
    }

    ErrorAlert(
        error = error,
        onDismiss = {
            quesionBankViewModel.clearError()
            quesionBankViewModel.clearData()
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QuestionItem(
    questionBankViewModel: QuestionBankViewModel,
    question: QuestionBankResponse,
    onImageClick: (QuestionBankResponse) -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
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
                                    .sharedElement(
                                        sharedContentState  = rememberSharedContentState(key = "image-${question.id}"),
                                        animatedVisibilityScope = animatedContentScope,
                                        boundsTransform = { _, _ ->
                                            spring(stiffness = Spring.StiffnessMedium)
                                        }
                                    )
                            )
                        }

                    }

                    Spacer(Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        question.choices.forEach { choice ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (choice.is_correct) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceContainer
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = choice.text,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (choice.is_correct) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Icon(
                imageVector = if (question.isFavorite) EvaIcons.Fill.Star else EvaIcons.Outline.Star,
                contentDescription = "Favorito",
                tint = if (question.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(100)
                    )
                    .padding(4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { questionBankViewModel.toggleFavorite(question) }
                    )
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun FullScreenImageTransition(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Expanded image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

