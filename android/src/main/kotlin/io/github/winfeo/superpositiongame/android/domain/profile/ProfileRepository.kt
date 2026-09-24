package io.github.winfeo.superpositiongame.android.domain.profile

import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryItem

interface ProfileRepository {
    suspend fun getUserProfile(userId: Long): Result<AuthorizedUser>
    suspend fun updateNickname(userId: Long, newNickname: String): Result<AuthorizedUser>
    suspend fun getGameHistory(userId: Long): Result<List<GameHistoryItem>>
}
