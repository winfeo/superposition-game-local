package io.github.winfeo.superpositiongame.android.domain.settings

import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser

interface AccountRepository {
    suspend fun updateEmail(userId: Long, newEmail: String): Result<AuthorizedUser>
    suspend fun deleteAccount(userId: Long): Result<Unit>
}
