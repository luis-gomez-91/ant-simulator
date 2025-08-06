package org.itb.nominas.core.navigation

import kotlinx.serialization.Serializable

interface Screen {
    val route: String
}

@Serializable
object HomeRoute : Screen {
    override val route = "home"
}

@Serializable
data class OptionsRoute(val licenceId: Int) : Screen {
    override val route: String
        get() = "options/$licenceId"
}