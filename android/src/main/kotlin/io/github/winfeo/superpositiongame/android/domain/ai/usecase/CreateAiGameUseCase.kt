package io.github.winfeo.superpositiongame.android.domain.ai.usecase

import io.github.winfeo.superpositiongame.android.domain.ai.AiGameRepository
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiDifficulty

class CreateAiGameUseCase(
    private val repository: AiGameRepository
) {
    suspend operator fun invoke(difficulty: AiDifficulty): Result<String> {
        return repository.createGame(difficulty)
    }
}
