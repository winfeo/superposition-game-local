package io.github.winfeo.superpositiongame.android.domain.ai

import io.github.winfeo.superpositiongame.android.domain.ai.model.AiDifficulty

interface AiGameRepository {
    suspend fun createGame(difficulty: AiDifficulty): Result<String>
}
