package org.itb.nominas.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.luisdev.antsimulator.core.utils.Theme
import com.luisdev.antsimulator.core.utils.getTheme
import org.itb.nominas.core.navigation.HomeRoute
import org.itb.nominas.core.utils.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    content: @Composable () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showBottomSheetTheme by mainViewModel.bottomSheetTheme.collectAsState(false)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isHomeSelected = currentRoute == HomeRoute::class.qualifiedName

    Scaffold(
        topBar = {
            if (!isHomeSelected) {
                MainTopBar(mainViewModel, navController)
            }
        },
        bottomBar = { MainBottomBar(navController, mainViewModel, isHomeSelected) }
    ) { innerPadding ->
        Surface(
            Modifier
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 4.dp)
            ) {
                content()
            }
        }
    }

    if (showBottomSheetTheme) {
        ThemeSettings(mainViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettings (
    mainViewModel: MainViewModel
) {
    val theme by mainViewModel.selectedTheme.collectAsState()
    val themes = listOf(Theme.Light, Theme.Dark, Theme.SystemDefault)

    ModalBottomSheet(
        onDismissRequest = { mainViewModel.setBottomSheetTheme(false) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Seleccionar Tema",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
            }

            items(themes) { appTheme ->
                val themeItem = appTheme.getTheme()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { mainViewModel.setTheme(appTheme) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (appTheme == theme) themeItem.iconSelect else themeItem.icon,
                            contentDescription = themeItem.text,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = themeItem.text,
                            style = if (appTheme == theme) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    RadioButton(
                        selected = appTheme == theme,
                        onClick = { mainViewModel.setTheme(appTheme) }
                    )
                }
                HorizontalDivider()
            }
        }

    }
}
