package org.itb.nominas.core.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.luisdev.antsimulator.core.utils.getTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Home
import compose.icons.evaicons.outline.Home
import com.luisdev.antsimulator.domain.BottomBarItem
import compose.icons.evaicons.outline.Moon
import org.itb.nominas.core.navigation.HomeRoute
import org.itb.nominas.core.utils.MainViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun MainBottomBar(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    isHomeSelected: Boolean
) {
    val selectedTheme by mainViewModel.selectedTheme.collectAsState(null)


    val navigationIcons = listOf<BottomBarItem>(
        BottomBarItem(
            onclick = { navController.navigate(HomeRoute) },
            label = "Inicio",
            icon = if (isHomeSelected) EvaIcons.Fill.Home else EvaIcons.Outline.Home,
            color = if (isHomeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelSmall,
            isSelected = isHomeSelected
        ),
        BottomBarItem(
            onclick = { mainViewModel.setBottomSheetTheme(true) },
            label = "Tema",
            icon = selectedTheme?.getTheme()?.icon ?: EvaIcons.Outline.Moon,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelSmall
        ),
    )

    Surface (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ){
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            navigationIcons.forEach {
                val animatedIconSize by animateDpAsState(
                    targetValue = 24.dp,
                    animationSpec = tween(durationMillis = 300)
                )

                val animatedColor by animateColorAsState(
                    targetValue = it.color,
                    animationSpec = tween(durationMillis = 300)
                )

                NavigationBarItem(
                    selected = it.isSelected,
                    onClick = { it.onclick() },
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.label,
                            tint = animatedColor,
                            modifier = Modifier.size(animatedIconSize)
                        )
                    },
                    label = {
                        Text(
                            text = it.label,
                            style = it.style,
                            color = animatedColor,
                            textAlign = TextAlign.Center,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = animatedColor,
                        unselectedIconColor = animatedColor,
                        selectedTextColor = animatedColor,
                        unselectedTextColor = animatedColor
                    )
                )
            }
        }
    }
}