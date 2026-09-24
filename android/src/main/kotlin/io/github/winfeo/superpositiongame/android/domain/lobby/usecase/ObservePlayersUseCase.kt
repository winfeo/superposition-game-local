package io.github.winfeo.superpositiongame.android.domain.lobby.usecase

import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player
import kotlinx.coroutines.flow.Flow

class ObservePlayersUseCase(
    private val repository: LobbyRepository,
) {
    operator fun invoke(currentUserId: String): Flow<List<Player>> {
        return repository.observePlayersInLobby(currentUserId)
    }
}
