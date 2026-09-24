package io.github.winfeo.superpositiongame.android.ui.screen.ai

import io.github.winfeo.superpositiongame.android.domain.ai.model.AiDifficulty
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiGameError

data class AiGameState(
    val selectedDifficulty: AiDifficulty = AiDifficulty.LOW,
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
    val error: AiGameError? = null
)
