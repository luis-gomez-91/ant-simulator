package com.luisdev.antsimulator.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.luisdev.antsimulator.features.home.ui.HomeScreen
import com.luisdev.antsimulator.features.options.OptionsScreen
import org.itb.nominas.core.navigation.HomeRoute
import org.itb.nominas.core.navigation.OptionsRoute

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> { HomeScreen(navController) }

        composable<OptionsRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<OptionsRoute>()
            OptionsScreen(
                licenceId = args.licenceId,
                navHostController = navController
            )
        }
    }
}
