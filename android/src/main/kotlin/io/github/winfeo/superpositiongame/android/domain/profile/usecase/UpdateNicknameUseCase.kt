package io.github.winfeo.superpositiongame.android.domain.profile.usecase

import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.github.winfeo.superpositiongame.android.domain.profile.ProfileRepository

class UpdateNicknameUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        userId: Long,
        newNickname: String
    ): Result<AuthorizedUser> {
        if (userId < 0) {
            return Result.failure(IllegalArgumentException("Неверный id пользователя"))
        }

        if (newNickname.isBlank()) {
            return Result.failure(IllegalArgumentException("Заполните поле"))
        }

        return repository.updateNickname(userId, newNickname)
    }
}
