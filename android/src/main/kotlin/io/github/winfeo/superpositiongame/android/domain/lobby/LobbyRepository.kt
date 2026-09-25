package io.github.winfeo.superpositiongame.android.domain.lobby

import io.github.winfeo.superpositiongame.android.domain.game.model.BoardSession
import kotlinx.coroutines.flow.StateFlow

interface LobbyRepository {
    val session: StateFlow<BoardSession>
    fun retryConnection()
    fun startGame(opponentId: Int): Boolean
}
