package io.github.winfeo.superpositiongame.android.domain.auth.usecase

import io.github.winfeo.superpositiongame.android.domain.auth.AuthRepository
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthToken
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Pair<AuthorizedUser, AuthToken>> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Заполните все поля"))
        }
        return authRepository.login(
            email = email,
            password = password
        )
    }
}
