package io.github.winfeo.superpositiongame.android.domain.lobby.usecase

import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository

class RetryBoardConnectionUseCase(
    private val repository: LobbyRepository
) {
    operator fun invoke() = repository.retryConnection()
}
