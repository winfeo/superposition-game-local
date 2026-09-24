package io.github.winfeo.superpositiongame.android.ui.screen.game

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.domain.game.PingRepository
import io.github.winfeo.superpositiongame.android.domain.game.model.GameLifecycleEvent
import io.github.winfeo.superpositiongame.android.domain.game.model.GameSessionStatus
import io.github.winfeo.superpositiongame.android.domain.game.model.TimerTimestamp
import io.github.winfeo.superpositiongame.android.domain.game.model.TimerUpdatePacket
import io.github.winfeo.superpositiongame.android.domain.game.usecase.ObserveGameStateUseCase
import io.github.winfeo.superpositiongame.android.domain.game.usecase.SendMoveUseCase
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameDialogState
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.GamePhase
import io.github.winfeo.superpositiongame.model.game.Move
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.ceil

//Хранит текущее состояние игры, принимает и отправляет ходы
class GameViewModel(
    private val playerId: String,
    private val gameId: String,
    private val gameRepository: GameRepository,
    private val pingRepository: PingRepository
): ViewModel() {
    private companion object {
        const val HEARTBEAT_INTERVAL_MS = 2_000L
    }

    private val observeGameStateUseCase = ObserveGameStateUseCase(gameRepository)
    private val sendMoveUseCase = SendMoveUseCase(gameRepository)

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState

    private val _dialogState = MutableStateFlow<GameDialogState?>(null)
    val dialogState: StateFlow<GameDialogState?> = _dialogState

    private val _timerSeconds = MutableStateFlow(0)
    val timerSeconds: StateFlow<Int> = _timerSeconds
    private var timerJob: Job? = null
    private var heartbeatJob: Job? = null
    private var presenceReconnectJob: Job? = null
    private var timerTimestamp: TimerTimestamp? = null
    private var isGameFinished = false
    private var isSessionPaused = false
    private var isGameVisible = false
    private var measuredOneWayDelayMs = 100L


    init {
        Log.d("GAME_MODEL", "Создание ViewModel")
//        observeGame()
//        startTimer()
        startGame()
    }

    private fun startGame() {
        observeGame()
        observeTimer()
        observeLifecycle()
        observeConnectionState()
        startTimerLoop()

        viewModelScope.launch {
            try {
                val rtt = pingRepository.measureRTT()
                measuredOneWayDelayMs = rtt / 2
                Log.d("GAME_SYNC", "RTT: ${rtt}ms, OneWayDelay: ${measuredOneWayDelayMs}ms")
            } catch (e: Exception) {
                Log.e("GAME_SYNC", "Ошибка: ${e.message}")
            }
        }
    }

    private fun observeGame() {
        viewModelScope.launch {
            observeGameStateUseCase(
                gameId = gameId,
                playerId = playerId
            ).collect { newState ->
                val previousState = _gameState.value

                if (previousState != null && (newState.turnNumber != previousState.turnNumber || newState.currentPlayerId != previousState.currentPlayerId)) {
                    dismissPendingTurnDialog()
                }

                _gameState.value = newState

                if (newState.phase == GamePhase.GAME_FINISHED) {
                    isGameFinished = true
                    timerTimestamp = null
                    _timerSeconds.value = 0
                    dismissPendingTurnDialog()
                    return@collect
                }

                val currentTimestamp = timerTimestamp
                if (currentTimestamp != null
                    && newState.turnNumber > currentTimestamp.turnNumber
                ) {
                    timerTimestamp = null
                    _timerSeconds.value = 0
                }
            }
        }
    }

    private fun observeTimer() {
        viewModelScope.launch {
            gameRepository.observeTimerUpdates(gameId).collect { packet ->
                applyTimerPacket(packet)
            }
        }
    }

    private fun applyTimerPacket(
        packet: TimerUpdatePacket
    ) {
        if (isGameFinished || isSessionPaused) return

        val gameTurnNumber = _gameState.value?.turnNumber
        if (gameTurnNumber != null && packet.turnNumber < gameTurnNumber) return

        val currentTimestamp = timerTimestamp
        if (currentTimestamp != null) {
            if (packet.turnNumber < currentTimestamp.turnNumber) return
            if (packet.turnNumber == currentTimestamp.turnNumber && packet.revision <= currentTimestamp.revision) return
        }

        val correctedTimeLeftMs = (packet.timeLeftMs - measuredOneWayDelayMs).coerceAtLeast(0L)
        timerTimestamp = TimerTimestamp(
            turnNumber = packet.turnNumber,
            revision = packet.revision,
            timeLeftAtReceiptMs = correctedTimeLeftMs,
            receivedAtRealtimeMs = SystemClock.elapsedRealtime()
        )

        _timerSeconds.value = ceil(correctedTimeLeftMs / 1_000.0).toInt()
    }

    private fun observeLifecycle() {
        viewModelScope.launch {
            gameRepository.observeGameLifecycle(gameId).collect { event ->
                handleLifecycleEvent(event)
            }
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            gameRepository.observeConnectionState()
                .filter { it }
                .collect {
                    if (isGameVisible) schedulePresenceReconnect()
                }
        }
    }

    private fun handleLifecycleEvent(event: GameLifecycleEvent) {
        when (event.status) {
            GameSessionStatus.PAUSED_FOR_RECONNECT -> {
                isSessionPaused = true
                timerTimestamp = null
                dismissPendingTurnDialog()

                if (playerId in event.disconnectedPlayerIds) {
                    if (isGameVisible) gameRepository.reconnectToGame(gameId)
                    return
                }

                val opponentId = event.disconnectedPlayerIds
                    .firstOrNull { it != playerId }?: return
                val opponentNickname = _gameState.value?.players?.get(opponentId)?.nickname

                _dialogState.value = GameDialogState.OpponentDisconnectedDialog(
                    opponentNickname = opponentNickname,
                    reconnectDeadline = event.reconnectDeadlines[opponentId],
                    serverTime = event.serverTime
                )
            }

            GameSessionStatus.ACTIVE -> {
                isSessionPaused = false
                if (_dialogState.value is GameDialogState.OpponentDisconnectedDialog) _dialogState.value = null
            }

            GameSessionStatus.FINISHED,
            GameSessionStatus.CANCELLED -> {
                isSessionPaused = false
                isGameFinished = true
                timerTimestamp = null
                _timerSeconds.value = 0
                dismissPendingTurnDialog()
            }

            GameSessionStatus.WAITING_FOR_PLAYERS,
            GameSessionStatus.UNKNOWN -> Unit
        }
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(100L)
                if (isGameFinished || isSessionPaused) continue

                val timer = timerTimestamp?: continue
                val elapsedSinceReceiptMs = SystemClock.elapsedRealtime() - timer.receivedAtRealtimeMs
                val timeLeftMs = (timer.timeLeftAtReceiptMs - elapsedSinceReceiptMs).coerceAtLeast(0L)

                _timerSeconds.value = ceil(timeLeftMs / 1_000.0).toInt()
            }
        }
    }

    fun onGameVisible() {
        if (isGameVisible) return
        isGameVisible = true
        schedulePresenceReconnect()

        heartbeatJob?.cancel()
        heartbeatJob = viewModelScope.launch {
            while (true) {
                gameRepository.sendHeartbeat(gameId)
                delay(HEARTBEAT_INTERVAL_MS)
            }
        }
    }

    fun onGameHidden() {
        if (!isGameVisible) return

        isGameVisible = false
        heartbeatJob?.cancel()
        heartbeatJob = null
        presenceReconnectJob?.cancel()
        presenceReconnectJob = null
        dismissPendingTurnDialog()

        if (_gameState.value?.phase != GamePhase.GAME_FINISHED) gameRepository.markGameInactive(gameId)
    }

    private fun schedulePresenceReconnect() {
        presenceReconnectJob?.cancel()
        presenceReconnectJob = viewModelScope.launch {
            delay(300L)
            if (isGameVisible) gameRepository.reconnectToGame(gameId)
        }
    }

    fun sendMove(move: Move) {
        val currentState = _gameState.value ?: return

        if (move !is Move.Surrender && (isGameFinished || isSessionPaused || currentState.currentPlayerId != playerId || currentState.turnNumber != move.expectedTurnNumber)) {
            Log.d("GAME_SEND_MOVE", "Ход отклонён на клиенте: устаревший контекст хода")
            return
        }

        viewModelScope.launch {
            Log.d("GAME_SEND_MOVE", "Отправка хода из viewModel, ход: ${move.type}")
            Log.d("GAME_SEND_MOVE", "gameId = '$gameId', move = ${move.type}")
            sendMoveUseCase(
                gameId = gameId,
                move = move
            )
        }
    }

    fun surrender() {
        val currentState = _gameState.value ?: return
        sendMove(
            Move.Surrender(
                playerId = playerId,
                expectedTurnNumber = currentState.turnNumber
            )
        )
    }

    fun showRotateCardDialog(
        availableStates: List<DiceState>,
        onStateSelected: (DiceState) -> Unit
    ) {
        _dialogState.value = GameDialogState.RotateDialog(
            availableStates = availableStates,
            onStateSelected = onStateSelected
        )
    }

    fun showReshuffleDialog(
        cards: List<Card>,
        maxSelectable: Int = 4,
        minSelectable: Int = 1,
        onCardsSelected: (List<Card>) -> Unit
    ) {
        _dialogState.value = GameDialogState.ReshuffleDialog(
            cards = cards,
            maxSelectable = maxSelectable,
            minSelectable = minSelectable,
            onCardsSelected = onCardsSelected
        )
    }

    fun showCardPreview(
        card: Card
    ) {
        _dialogState.value = GameDialogState.CardPreviewDialog(
            card = card
        )
    }

    fun showGameFinishedDialog(
        isWinner: Boolean,
        onReturnToLobby: () -> Unit
    ) {
        isGameFinished = true
        timerTimestamp = null
        _timerSeconds.value = 0

        _dialogState.value = GameDialogState.GameFinishedDialog(
            isWinner = isWinner,
            onReturnToLobby = onReturnToLobby
        )
    }

    fun showGameMenuDialog(
        onResume: () -> Unit,
        onRules: () -> Unit,
        onSettings: () -> Unit,
        onSurrender: () -> Unit,
        onDismiss: () -> Unit
    ) {
        _dialogState.value = GameDialogState.GameMenuDialog(
            onResume = onResume,
            onRules = onRules,
            onSettings = onSettings,
            onSurrender = onSurrender,
            onDismiss = onDismiss
        )
    }

    fun showRulesDialog() {
        _dialogState.value = GameDialogState.RulesDialog
    }

    fun dismissDialog() {
        _dialogState.value = null
    }

    private fun dismissPendingTurnDialog() {
        when (_dialogState.value) {
            is GameDialogState.RotateDialog, is GameDialogState.ReshuffleDialog -> _dialogState.value = null
            else -> Unit
        }
    }

    override fun onCleared() {
        super.onCleared()

        timerJob?.cancel()
        timerJob = null
        heartbeatJob?.cancel()
        heartbeatJob = null
        presenceReconnectJob?.cancel()
        presenceReconnectJob = null
    }
}
