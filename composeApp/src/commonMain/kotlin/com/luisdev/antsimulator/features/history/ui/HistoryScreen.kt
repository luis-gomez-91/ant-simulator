package com.luisdev.antsimulator.features.history.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.composable.BannerAd
import com.luisdev.antsimulator.core.ui.components.MyCard
import com.luisdev.antsimulator.core.ui.components.ShimmerLoadingAnimation
import com.luisdev.antsimulator.features.history.domain.HistoryEntry
import org.itb.nominas.core.components.MainScaffold
import org.itb.nominas.core.navigation.ResultRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun HistoryScreen(
    navHostController: NavHostController,
    historyViewModel: HistoryViewModel = koinViewModel()
) {
    val licence by historyViewModel.mainViewModel.licenceSelected.collectAsState(null)
    licence?.let {
        historyViewModel.loadTestHistory(it.id)
        historyViewModel.mainViewModel.setTitle("Licencia tipo ${it.name}")

    }

    MainScaffold(
        navController = navHostController,
        mainViewModel = historyViewModel.mainViewModel,
        content = {
            TestHistoryContent(
                testHistoryViewModel = historyViewModel,
                onTestClick = { simulationId ->
                    navHostController.navigate(ResultRoute(simulationId = simulationId))
                }
            )
        }
    )
}

@OptIn(DependsOnGoogleMobileAds::class)
@Composable
private fun TestHistoryContent(
    testHistoryViewModel: HistoryViewModel,
    onTestClick: (Long) -> Unit
) {
    val testHistory by testHistoryViewModel.testHistory.collectAsState()
    val isLoading by testHistoryViewModel.isLoading.collectAsState()
    val error by testHistoryViewModel.error.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading -> {
                    ShimmerLoadingAnimation(rowNumber = 5)
                }
                testHistory.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay tests realizados",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Realiza tu primer test para ver el historial aquí",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(testHistory) { testEntry ->
                            TestHistoryItem(
                                testEntry = testEntry,
                                testHistoryViewModel = testHistoryViewModel,
                                onClick = { onTestClick(testEntry.id) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        BannerAd()
    }

}

@Composable
private fun TestHistoryItem(
    testEntry: HistoryEntry,
    testHistoryViewModel: HistoryViewModel,
    onClick: () -> Unit
) {
    MyCard (
        onClick = onClick
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = testHistoryViewModel.formatDate(testEntry.date),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = testHistoryViewModel.formatTime(testEntry.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Preguntas: ${testEntry.totalQuestions}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = testHistoryViewModel.formatScore(testEntry.score, testEntry.totalQuestions),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Calcular porcentaje
                    val aprove = testEntry.score >= 17
                    Text(
                        text = if (aprove) "Aprobado" else "Reprobado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            aprove -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.error
                        },
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ver detalles",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

