package io.github.winfeo.superpositiongame.android.domain.auth

import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthToken
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser


interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Pair<AuthorizedUser, AuthToken>>
    suspend fun register(email: String, password: String): Result<AuthorizedUser>
}
