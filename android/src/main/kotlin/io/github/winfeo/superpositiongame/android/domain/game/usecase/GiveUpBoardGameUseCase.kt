package io.github.winfeo.superpositiongame.android.domain.game.usecase

import io.github.winfeo.superpositiongame.android.domain.game.GameRepository

class GiveUpBoardGameUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(): Boolean = repository.giveUp()
}
