package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository

class GameRepositoryImpl(
    private val network: Network
) : GameRepository {
    override val session = network.state

    override fun selectTarget(playerIndex: Int, cubitIndex: Int): Boolean {
        return network.selectTarget(playerIndex, cubitIndex)
    }

    override fun giveUp(): Boolean {
        return network.giveUp()
    }

    override fun returnToLobby() {
        return network.returnToLobby()
    }
}
