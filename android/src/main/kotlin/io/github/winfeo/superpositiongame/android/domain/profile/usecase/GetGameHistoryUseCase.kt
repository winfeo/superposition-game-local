package io.github.winfeo.superpositiongame.android.domain.profile.usecase

import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryItem
import io.github.winfeo.superpositiongame.android.domain.profile.ProfileRepository

class GetGameHistoryUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: Long): Result<List<GameHistoryItem>> {
        if (userId < 0) {
            return Result.failure(IllegalArgumentException("Неверный id пользователя"))
        }

        return repository.getGameHistory(userId)
    }
}
