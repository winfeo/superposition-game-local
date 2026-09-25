package io.github.winfeo.superpositiongame.android.ui.screen.lobby

import androidx.lifecycle.ViewModel
import io.github.winfeo.superpositiongame.android.domain.lobby.LobbyRepository
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.ObserveBoardLobbyUseCase
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.RetryBoardConnectionUseCase
import io.github.winfeo.superpositiongame.android.domain.lobby.usecase.StartBoardGameUseCase

class LobbyViewModel(
    private val repository: LobbyRepository
) : ViewModel() {
    private val observeBoardLobby = ObserveBoardLobbyUseCase(repository)
    private val retryBoardConnection = RetryBoardConnectionUseCase(repository)
    private val startBoardGame = StartBoardGameUseCase(repository)

    val session = observeBoardLobby()

    fun retryConnection() {
        retryBoardConnection()
    }
    fun startGame(opponentId: Int): Boolean {
        return startBoardGame(opponentId)
    }
}
