package com.luisdev.antsimulator.core.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.luisdev.antsimulator.features.history.ui.HistoryScreen
import com.luisdev.antsimulator.features.home.ui.HomeScreen
import com.luisdev.antsimulator.features.options.OptionsScreen
import com.luisdev.antsimulator.features.question_bank.ui.QuestionBankScreen
import com.luisdev.antsimulator.features.result.ui.ResultScreen
import com.luisdev.antsimulator.features.simulator.ui.SimulatorScreen
import org.itb.nominas.core.navigation.HistoryRoute
import org.itb.nominas.core.navigation.HomeRoute
import org.itb.nominas.core.navigation.OptionsRoute
import org.itb.nominas.core.navigation.QuestionBankRoute
import org.itb.nominas.core.navigation.ResultRoute
import org.itb.nominas.core.navigation.SimulatorRoute

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationWrapper(activity: Any?) {
    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = HomeRoute) {
            composable<HomeRoute> { HomeScreen(navController, animatedContentScope = this, sharedTransitionScope = this@SharedTransitionLayout,) }

            composable<OptionsRoute> { backStackEntry ->
                val args = backStackEntry.toRoute<OptionsRoute>()
                OptionsScreen(
                    licenceId = args.licenceId,
                    navHostController = navController,
                    animatedContentScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout,
                )
            }

            composable<QuestionBankRoute> {
                QuestionBankScreen(
                    navHostController = navController,
                    animatedContentScope = this,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }

            composable<SimulatorRoute> {
                SimulatorScreen(
                    navHostController = navController,
                    activity = activity
                )
            }

            composable<ResultRoute> { backStackEntry ->
                val args = backStackEntry.toRoute<ResultRoute>()
                ResultScreen(args.simulationId, navController)
            }

            composable<HistoryRoute> { backStackEntry ->
                HistoryScreen(navController)
            }

        }
    }

}
