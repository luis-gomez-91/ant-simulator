package com.luisdev.antsimulator.features.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.luisdev.antsimulator.core.ui.components.MyCard
import com.luisdev.antsimulator.core.ui.components.MyErrorAlert
import com.luisdev.antsimulator.core.ui.components.ShimmerLoadingAnimation
import com.luisdev.antsimulator.features.home.data.LicenceResponse
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ChevronDown
import compose.icons.evaicons.outline.ChevronUp
import org.itb.nominas.core.components.MainScaffold
import org.itb.nominas.core.navigation.OptionsRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    MainScaffold(
        navController = navHostController,
        mainViewModel = homeViewModel.mainViewModel,
        content = { Screen(homeViewModel, navHostController) }
    )
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    navHostController: NavHostController
) {
    val data by homeViewModel.data.collectAsState(null)
    val error by homeViewModel.error.collectAsState(null)
    val mainError by homeViewModel.mainViewModel.error.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)

    LaunchedEffect(Unit) {
        homeViewModel.loadHome()
    }

    if (isLoading) {
        Column (
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            ShimmerLoadingAnimation(3)
        }
    } else {
        data?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(it) { licence ->
                    LicenceItem(licence, navHostController, homeViewModel)
                }
            }
        }
    }

    error?.let {
        MyErrorAlert(
            titulo = "Error",
            mensaje = it.message,
            onDismiss = {
                homeViewModel.clearError()
                homeViewModel.clearData()
            },
            showAlert = true
        )
    }

    mainError?.let {
        MyErrorAlert(
            titulo = "Error",
            mensaje = it.message,
            onDismiss = {
                homeViewModel.mainViewModel.clearError()
            },
            showAlert = true
        )
    }
}


@Composable
fun LicenceItem(
    licence: LicenceResponse,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    var expand by remember { mutableStateOf(false) }

    MyCard (
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        onClick = {
            homeViewModel.mainViewModel.setLicenteSelected(licence)
            navController.navigate(OptionsRoute(licence.id)) {
                launchSingleTop = true
            }
        }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = licence.image,
                contentDescription = licence.name,
                modifier = Modifier.width(40.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column (
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Licencia tipo ${licence.name}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    IconButton(onClick = { expand = !expand }) {
                        Icon(
                            imageVector = if (expand) EvaIcons.Outline.ChevronUp else EvaIcons.Outline.ChevronDown,
                            contentDescription = "Expandir o colapsar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                AnimatedVisibility(
                    visible = expand,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = licence.description,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}