package io.github.winfeo.superpositiongame.android.domain.game.usecase

import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.domain.game.model.ActiveGame
import kotlinx.coroutines.flow.Flow

class ObserveActiveGameUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(): Flow<ActiveGame?> {
        return gameRepository.observeActiveGame()
    }
}
