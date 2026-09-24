package io.github.winfeo.superpositiongame.android.domain.history

interface GameHistoryRepository {
    suspend fun getGameHistory(userId: Long): Result<List<GameHistoryItem>>
}
