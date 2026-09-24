package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.source.rest.GameHistoryApi
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryItem
import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryRepository

class GameHistoryRepositoryImpl(
    private val api: GameHistoryApi
): GameHistoryRepository {
    override suspend fun getGameHistory(userId: Long): Result<List<GameHistoryItem>> {
        return try {
            val dto = api.getGameHistory(userId)
            val history = dto.map { it.toDomain() }
            Result.success(history)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
