package com.luisdev.antsimulator.core.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.luisdev.antsimulator.features.home.ui.HomeScreen
import com.luisdev.antsimulator.features.options.OptionsScreen
import com.luisdev.antsimulator.features.question_bank.ui.QuestionBankScreen
import org.itb.nominas.core.navigation.HomeRoute
import org.itb.nominas.core.navigation.OptionsRoute
import org.itb.nominas.core.navigation.QuestionBankRoute

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationWrapper() {
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

        }
    }

}
