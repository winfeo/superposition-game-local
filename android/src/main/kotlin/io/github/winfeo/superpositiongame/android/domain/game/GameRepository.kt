package io.github.winfeo.superpositiongame.android.domain.game

import io.github.winfeo.superpositiongame.android.domain.game.model.BoardSession
import kotlinx.coroutines.flow.StateFlow

interface GameRepository {
    val session: StateFlow<BoardSession>
    fun selectTarget(playerIndex: Int, cubitIndex: Int): Boolean
    fun giveUp(): Boolean
    fun returnToLobby()
}
