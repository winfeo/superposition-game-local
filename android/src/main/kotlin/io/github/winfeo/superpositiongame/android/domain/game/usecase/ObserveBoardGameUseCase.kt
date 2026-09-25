package io.github.winfeo.superpositiongame.android.domain.game.usecase

import io.github.winfeo.superpositiongame.android.domain.game.GameRepository

class ObserveBoardGameUseCase(
    private val repository: GameRepository
) {
    operator fun invoke() = repository.session
}
