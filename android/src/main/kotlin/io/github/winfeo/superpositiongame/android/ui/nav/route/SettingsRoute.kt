package io.github.winfeo.superpositiongame.android.ui.nav.route

import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute: AppRoute {
    override val route: String = "settings"
}
