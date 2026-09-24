package io.github.winfeo.superpositiongame.android.ui.nav.route

import kotlinx.serialization.Serializable

@Serializable
data object ProfileRoute: AppRoute {
    override val route: String = "profile"
}
