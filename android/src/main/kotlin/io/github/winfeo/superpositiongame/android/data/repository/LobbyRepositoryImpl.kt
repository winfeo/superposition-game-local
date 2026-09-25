package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository

class LobbyRepositoryImpl(
    private val network: Network
) : LobbyRepository {
    override val session = network.state

    override fun retryConnection() {
        return network.retry()
    }

    override fun startGame(opponentId: Int): Boolean {
        return network.startGame(opponentId)
    }
}
