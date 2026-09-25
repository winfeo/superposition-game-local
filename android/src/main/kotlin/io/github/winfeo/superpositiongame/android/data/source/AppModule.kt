package io.github.winfeo.superpositiongame.android.data.source

import android.content.Context
import io.github.winfeo.superpositiongame.android.data.repository.GameRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.repository.LobbyRepositoryImpl
import io.github.winfeo.superpositiongame.android.data.source.socket.Network
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AppModule {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var network: Network

    lateinit var lobbyRepository: LobbyRepository
        private set
    lateinit var gameRepository: GameRepository
        private set

    fun init(context: Context) {
        if (::network.isInitialized) return

        network = Network(context.applicationContext)
        lobbyRepository = LobbyRepositoryImpl(network)
        gameRepository = GameRepositoryImpl(network)
        network.start(scope)
    }
}
