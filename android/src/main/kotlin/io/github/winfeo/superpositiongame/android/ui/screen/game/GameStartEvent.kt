package io.github.winfeo.superpositiongame.android.ui.screen.game

import kotlinx.serialization.Serializable

@Serializable
data class GameStartEvent(
    val gameId: String
)
