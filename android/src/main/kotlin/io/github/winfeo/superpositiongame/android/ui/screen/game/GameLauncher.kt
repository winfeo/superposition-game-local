package io.github.winfeo.superpositiongame.android.ui.screen.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.domain.game.model.ActiveGame
import io.github.winfeo.superpositiongame.android.domain.game.model.GameSessionStatus
import io.github.winfeo.superpositiongame.android.domain.game.usecase.ObserveActiveGameUseCase
import io.github.winfeo.superpositiongame.android.domain.game.usecase.ObserveGameStartUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameLauncher(
    private val playerId: String,
    private val repository: GameRepository
): ViewModel() {
    private val observeGameStartUseCase = ObserveGameStartUseCase(repository)
    private val observeActiveGameUseCase = ObserveActiveGameUseCase(repository)

    private val _gameFlow = MutableStateFlow<String?>(null)
    val gameFlow: StateFlow<String?> = _gameFlow.asStateFlow()

    private val _activeGame = MutableStateFlow<ActiveGame?>(null)
    val activeGame: StateFlow<ActiveGame?> = _activeGame.asStateFlow()

    private val _isReconnectInProgress = MutableStateFlow(false)
    val isReconnectInProgress: StateFlow<Boolean> =
        _isReconnectInProgress.asStateFlow()

    private var gameStartJob: Job? = null
    private var activeGameJob: Job? = null
    private var activeGameLifecycleJob: Job? = null
    private var activeGameRequestJob: Job? = null
    private var reconnectConfirmationJob: Job? = null
    private var openedGameId: String? = null
    private var observedLifecycleGameId: String? = null

    init {
        gameStartJob = viewModelScope.launch {
            observeGameStartUseCase().collect { gameId ->
                openGame(gameId)
            }
        }

        activeGameJob = viewModelScope.launch {
            observeActiveGameUseCase().collect { game ->
                if (openedGameId == null && _gameFlow.value == null) {
                    _activeGame.value = game
                    if (game == null) {
                        activeGameLifecycleJob?.cancel()
                        activeGameLifecycleJob = null
                        observedLifecycleGameId = null
                    } else {
                        observeActiveGameLifecycle(game.gameId)
                    }
                }
            }
        }
    }

    fun reconnectToGame() {
        val game = _activeGame.value ?: return
        if (_isReconnectInProgress.value) return

        _isReconnectInProgress.value = true
        repository.reconnectToGame(game.gameId)

        reconnectConfirmationJob?.cancel()
        reconnectConfirmationJob = viewModelScope.launch {
            delay(5_000L)
            if (_isReconnectInProgress.value) {
                _isReconnectInProgress.value = false
                repository.requestActiveGame()
            }
        }
    }

    fun declineReconnect() {
        val gameId = _activeGame.value?.gameId ?: return
        if (_isReconnectInProgress.value) return
        _activeGame.value = null
        activeGameLifecycleJob?.cancel()
        activeGameLifecycleJob = null
        observedLifecycleGameId = null
        repository.declineReconnect(gameId)
    }

    fun onReconnectExpired() {
        if (_isReconnectInProgress.value) return
        _activeGame.value = null
        repository.requestActiveGame()
    }

    fun onGameActivityClosed() {
        openedGameId = null
        activeGameRequestJob?.cancel()
        activeGameRequestJob = viewModelScope.launch {
            delay(500L)
            repository.requestActiveGame()
        }
    }

    fun resetGameId() {
        _gameFlow.value = null
    }

    fun onAiGameCreated(gameId: String) {
        openGame(gameId)
    }

    private fun openGame(gameId: String) {
        if (openedGameId == gameId) return
        openedGameId = gameId
        _isReconnectInProgress.value = false
        reconnectConfirmationJob?.cancel()
        _activeGame.value = null
        activeGameLifecycleJob?.cancel()
        activeGameLifecycleJob = null
        observedLifecycleGameId = null
        _gameFlow.value = gameId
    }

    private fun observeActiveGameLifecycle(gameId: String) {
        if (activeGameLifecycleJob?.isActive == true
            && observedLifecycleGameId == gameId
        ) {
            return
        }

        activeGameLifecycleJob?.cancel()
        observedLifecycleGameId = gameId
        activeGameLifecycleJob = viewModelScope.launch {
            repository.observeGameLifecycle(gameId).collect { event ->
                if (event.status == GameSessionStatus.FINISHED
                    || event.status == GameSessionStatus.CANCELLED
                ) {
                    _isReconnectInProgress.value = false
                    reconnectConfirmationJob?.cancel()
                    _activeGame.value = null
                    observedLifecycleGameId = null
                    activeGameLifecycleJob?.cancel()
                    activeGameLifecycleJob = null
                    return@collect
                }

                if (_isReconnectInProgress.value
                    && playerId !in event.disconnectedPlayerIds
                ) {
                    _isReconnectInProgress.value = false
                    reconnectConfirmationJob?.cancel()
                    openGame(gameId)
                    return@collect
                }

                val currentGame = _activeGame.value ?: return@collect
                _activeGame.value = currentGame.copy(
                    status = event.status,
                    currentPlayerDisconnected =
                        playerId in event.disconnectedPlayerIds,
                    reconnectDeadline = event.reconnectDeadlines[playerId],
                    serverTime = event.serverTime
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameStartJob?.cancel()
        activeGameJob?.cancel()
        activeGameLifecycleJob?.cancel()
        activeGameRequestJob?.cancel()
        reconnectConfirmationJob?.cancel()
    }
}
