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

@Serializable
object QuestionBankRoute : Screen {
    override val route = "questionBank"
}

@Serializable
object SimulatorRoute : Screen {
    override val route = "simulator"
}

@Serializable
data class ResultRoute(val simulationId: Long) : Screen {
    override val route: String
        get() = "result/$simulationId"
}

@Serializable
object HistoryRoute : Screen {
    override val route = "history"
}