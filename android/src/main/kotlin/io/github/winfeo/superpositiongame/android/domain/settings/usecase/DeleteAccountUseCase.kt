package io.github.winfeo.superpositiongame.android.domain.settings.usecase

import io.github.winfeo.superpositiongame.android.domain.settings.AccountRepository

class DeleteAccountUseCase(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(userId: Long): Result<Unit> {
        if (userId < 0) {
            return Result.failure(IllegalArgumentException("Неверный id пользователя"))
        }

        return accountRepository.deleteAccount(userId)
    }
}
