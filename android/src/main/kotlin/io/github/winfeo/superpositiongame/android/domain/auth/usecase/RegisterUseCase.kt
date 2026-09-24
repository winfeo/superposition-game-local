package io.github.winfeo.superpositiongame.android.domain.auth.usecase

import io.github.winfeo.superpositiongame.android.domain.auth.AuthRepository
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<AuthorizedUser> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Заполните все поля"))
        }
        return authRepository.register(
            email = email,
            password = password
        )
    }
}
