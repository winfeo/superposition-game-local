package io.github.winfeo.superpositiongame.android.domain.game

import io.github.winfeo.superpositiongame.android.domain.game.model.ActiveGame
import io.github.winfeo.superpositiongame.android.domain.game.model.GameLifecycleEvent
import io.github.winfeo.superpositiongame.android.domain.game.model.TimerUpdatePacket
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun sendMove(gameId: String, move: Move)
    fun observeGameState(gameId: String, playerId: String): Flow<GameState>
    fun observeGameStart(): Flow<String>
    fun observeTimerUpdates(gameId: String): Flow<TimerUpdatePacket>
    fun observeConnectionState(): Flow<Boolean>
    fun observeActiveGame(): Flow<ActiveGame?>
    fun observeGameLifecycle(gameId: String): Flow<GameLifecycleEvent>
    fun requestActiveGame()
    fun sendHeartbeat(gameId: String)
    fun reconnectToGame(gameId: String)
    fun markGameInactive(gameId: String)
    fun declineReconnect(gameId: String)
}
