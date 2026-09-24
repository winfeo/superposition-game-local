package io.github.winfeo.superpositiongame.android.domain.history

class GetGameHistoryUseCase(
    private val repository: GameHistoryRepository
) {
    suspend operator fun invoke(userId: Long): Result<List<GameHistoryItem>> {
        if (userId < 0) {
            return Result.failure(IllegalArgumentException("Неверный id пользователя"))
        }
        return repository.getGameHistory(userId)
    }
}
