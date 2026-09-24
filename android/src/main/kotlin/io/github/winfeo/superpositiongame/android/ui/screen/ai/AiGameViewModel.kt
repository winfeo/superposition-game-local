package io.github.winfeo.superpositiongame.android.ui.screen.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.data.source.local.SettingsManager
import io.github.winfeo.superpositiongame.android.domain.ai.AiGameRepository
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiDifficulty
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiGameError
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiGameException
import io.github.winfeo.superpositiongame.android.domain.ai.usecase.CreateAiGameUseCase
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AiGameViewModel(
    aiGameRepository: AiGameRepository,
    gameRepository: GameRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val createAiGameUseCase = CreateAiGameUseCase(aiGameRepository)

    private val _state = MutableStateFlow(
        AiGameState(selectedDifficulty = settingsManager.aiDifficulty.value)
    )
    val state: StateFlow<AiGameState> = _state.asStateFlow()

    private val _gameCreated = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val gameCreated: SharedFlow<String> = _gameCreated.asSharedFlow()

    init {
        viewModelScope.launch {
            settingsManager.aiDifficulty.collect { difficulty ->
                _state.value = _state.value.copy(selectedDifficulty = difficulty)
            }
        }
        viewModelScope.launch {
            gameRepository.observeConnectionState().collect { isConnected ->
                val currentState = _state.value
                _state.value = currentState.copy(
                    isConnected = isConnected,
                    error =
                        if (isConnected && currentState.error == AiGameError.NO_CONNECTION) null
                        else currentState.error
                )
            }
        }
    }

    fun selectDifficulty(difficulty: AiDifficulty) {
        if (_state.value.isLoading) return

        _state.value = _state.value.copy(
            selectedDifficulty = difficulty,
            error = null
        )
        settingsManager.setAiDifficulty(difficulty)
    }

    fun startGame() {
        val currentState = _state.value

        if (currentState.isLoading) return
        if (!currentState.isConnected) {
            _state.value = currentState.copy(error = AiGameError.NO_CONNECTION)
            return
        }

        _state.value = currentState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            createAiGameUseCase(currentState.selectedDifficulty).fold(
                onSuccess = { gameId ->
                    _state.value = _state.value.copy(isLoading = false)
                    _gameCreated.emit(gameId)
                },
                onFailure = { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = (exception as? AiGameException)?.error?: AiGameError.UNKNOWN
                    )
                }
            )
        }
    }
}
