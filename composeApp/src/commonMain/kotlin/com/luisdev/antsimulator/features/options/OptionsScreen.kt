package com.luisdev.antsimulator.features.options

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.luisdev.antsimulator.domain.OptionButtomItem
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.BookOpen
import compose.icons.evaicons.outline.Car
import compose.icons.evaicons.outline.Download
import compose.icons.evaicons.outline.FileText
import org.itb.nominas.core.components.MainScaffold
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.composable.BannerAd
import org.itb.nominas.core.navigation.HistoryRoute
import org.itb.nominas.core.navigation.QuestionBankRoute
import org.itb.nominas.core.navigation.SimulatorRoute

@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
fun OptionsScreen(
    licenceId: Int,
    navHostController: NavHostController,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    optionsViewModel: OptionsViewModel = koinViewModel()
) {
    optionsViewModel.mainViewModel.setTitle(null)

    MainScaffold(
        navController = navHostController,
        mainViewModel = optionsViewModel.mainViewModel,
        content = { Screen(navHostController, optionsViewModel, animatedContentScope, sharedTransitionScope) }
    )
}


@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class,
    DependsOnGoogleMobileAds::class
)
@Composable
fun Screen(
    navHostController: NavHostController,
    optionsViewModel: OptionsViewModel,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val licenceSelected by optionsViewModel.mainViewModel.licenceSelected.collectAsState()

    licenceSelected?.let { licence ->
        val options = listOf<OptionButtomItem>(
            OptionButtomItem(
                text = "Iniciar Simulador",
                icon = EvaIcons.Outline.BookOpen,
                onclick = { navHostController.navigate(SimulatorRoute) }
            ),
            OptionButtomItem(
                text = "Banco de Preguntas",
                icon = EvaIcons.Outline.FileText,
                onclick = { navHostController.navigate(QuestionBankRoute) }
            ),
            OptionButtomItem(
                text = "Descargar PDF",
                icon = EvaIcons.Outline.Download,
                onclick = { optionsViewModel.mainViewModel.urlOpener.openURL(licence.question_bank) }
            ),
            OptionButtomItem(
                text = "Historial",
                icon = EvaIcons.Outline.Car,
                onclick = { navHostController.navigate(HistoryRoute) }
            ),
        )

        with(sharedTransitionScope) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn (
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        AsyncImage(
                            model = licence.image,
                            contentDescription = licence.name,
                            modifier = Modifier
                                .width(80.dp)
                                .sharedElement(
                                    sharedContentState  = rememberSharedContentState(key = "image-${licence.id}"),
                                    animatedVisibilityScope = animatedContentScope,
                                    boundsTransform = { _, _ ->
                                        spring(stiffness = Spring.StiffnessMedium)
                                    }
                                )
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Licencia tipo ${licence.name}",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = licence.description,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(300.dp)
                        )

                        Spacer(Modifier.height(32.dp))
                    }

                    items(options) { option ->
                        FilledTonalButton(
                            onClick = {
                                option.onclick()
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = option.text,
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ){
                                    Text(
                                        text = option.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(4.dp))
                BannerAd()
            }

        }

    }
}