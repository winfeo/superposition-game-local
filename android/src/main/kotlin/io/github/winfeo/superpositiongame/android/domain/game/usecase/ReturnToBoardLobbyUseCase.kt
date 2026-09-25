package io.github.winfeo.superpositiongame.android.domain.game.usecase

import io.github.winfeo.superpositiongame.android.domain.game.GameRepository

class ReturnToBoardLobbyUseCase(
    private val repository: GameRepository
) {
    operator fun invoke() = repository.returnToLobby()
}
