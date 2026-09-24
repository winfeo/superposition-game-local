package io.github.winfeo.superpositiongame.android.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryRepository
import io.github.winfeo.superpositiongame.android.domain.history.GetGameHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameHistoryViewModel(
    private val repository: GameHistoryRepository
) : ViewModel() {
    private val getGameHistoryUseCase = GetGameHistoryUseCase(repository)

    private val _state = MutableStateFlow(GameHistoryState())
    val state: StateFlow<GameHistoryState> = _state.asStateFlow()

    fun loadHistory(userId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = getGameHistoryUseCase(userId)
            result.fold(
                onSuccess = { history ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        history = history
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }
}
