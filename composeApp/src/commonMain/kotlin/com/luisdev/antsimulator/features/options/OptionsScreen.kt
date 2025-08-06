package com.luisdev.antsimulator.features.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import com.luisdev.antsimulator.core.ui.components.MyFilledTonalButton
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Eye
import compose.icons.evaicons.outline.File
import compose.icons.evaicons.outline.Shake
import compose.icons.evaicons.outline.Text
import org.itb.nominas.core.components.MainScaffold
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun OptionsScreen(
    licenceId: Int,
    navHostController: NavHostController,
    optionsViewModel: OptionsViewModel = koinViewModel()
) {
    MainScaffold(
        navController = navHostController,
        mainViewModel = optionsViewModel.mainViewModel,
        content = { Screen(navHostController, optionsViewModel) }
    )
}


@OptIn(KoinExperimentalAPI::class)
@Composable
fun Screen(
    navHostController: NavHostController,
    optionsViewModel: OptionsViewModel
) {

    val licenceSelected by optionsViewModel.mainViewModel.licenceSelected.collectAsState()

    licenceSelected?.let { licence ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                AsyncImage(
                    model = licence.image,
                    contentDescription = licence.name,
                    modifier = Modifier.width(48.dp)
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

            item {
                MyFilledTonalButton(
                    text = "Realizar Test",
                    buttonColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.primary,
                    icon = EvaIcons.Outline.Shake,
                    onClickAction = { },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                MyFilledTonalButton(
                    text = "Ver cuestionario",
                    buttonColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.primary,
                    icon = EvaIcons.Outline.File,
                    onClickAction = { },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                MyFilledTonalButton(
                    text = "Historial",
                    buttonColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.primary,
                    icon = EvaIcons.Outline.Eye,
                    onClickAction = { },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }


}