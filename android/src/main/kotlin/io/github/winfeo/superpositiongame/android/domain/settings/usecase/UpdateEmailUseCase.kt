package io.github.winfeo.superpositiongame.android.domain.settings.usecase

import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.github.winfeo.superpositiongame.android.domain.settings.AccountRepository

class UpdateEmailUseCase(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        userId: Long,
        newEmail: String
    ): Result<AuthorizedUser> {
        if (userId < 0) {
            return Result.failure(IllegalArgumentException("Неверный id пользователя"))
        }

        if (newEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("Заполните поле"))
        }

        return accountRepository.updateEmail(userId, newEmail)
    }
}
