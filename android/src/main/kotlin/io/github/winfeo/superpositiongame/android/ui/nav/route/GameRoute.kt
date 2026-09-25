package io.github.winfeo.superpositiongame.android.ui.nav.route

import kotlinx.serialization.Serializable

@Serializable
data object GameRoute: AppRoute {
    override val route: String = "game"
}
