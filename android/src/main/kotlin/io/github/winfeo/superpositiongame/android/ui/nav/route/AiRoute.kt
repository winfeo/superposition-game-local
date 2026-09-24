package io.github.winfeo.superpositiongame.android.ui.nav.route

import kotlinx.serialization.Serializable

@Serializable
data object AiRoute : AppRoute {
    override val route: String = "ai"
}
