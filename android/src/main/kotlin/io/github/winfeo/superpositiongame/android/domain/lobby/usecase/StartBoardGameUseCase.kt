package io.github.winfeo.superpositiongame.android.domain.lobby.usecase

import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository

class StartBoardGameUseCase(
    private val repository: LobbyRepository
) {
    operator fun invoke(opponentId: Int): Boolean = repository.startGame(opponentId)
}
