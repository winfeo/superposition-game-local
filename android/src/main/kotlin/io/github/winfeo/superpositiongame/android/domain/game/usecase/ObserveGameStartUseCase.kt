package io.github.winfeo.superpositiongame.android.domain.game.usecase

import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import kotlinx.coroutines.flow.Flow

class ObserveGameStartUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(): Flow<String> = gameRepository.observeGameStart()
}
