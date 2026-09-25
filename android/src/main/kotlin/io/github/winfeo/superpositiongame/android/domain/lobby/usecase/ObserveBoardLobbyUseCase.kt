package io.github.winfeo.superpositiongame.android.domain.lobby.usecase

import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository

class ObserveBoardLobbyUseCase(
    private val repository: LobbyRepository
) {
    operator fun invoke() = repository.session
}
