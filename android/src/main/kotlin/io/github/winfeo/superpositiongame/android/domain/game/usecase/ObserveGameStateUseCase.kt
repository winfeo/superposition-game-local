package io.github.winfeo.superpositiongame.android.domain.game.usecase

import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.model.game.GameState
import kotlinx.coroutines.flow.Flow

class ObserveGameStateUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(gameId: String, playerId: String): Flow<GameState> {
        return gameRepository.observeGameState(gameId, playerId)
    }
}
