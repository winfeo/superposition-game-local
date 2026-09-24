package io.github.winfeo.superpositiongame.android.ui.screen.history

import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryItem

data class GameHistoryState(
    val isLoading: Boolean = false,
    val history: List<GameHistoryItem> = emptyList(),
    val error: String? = null
)
