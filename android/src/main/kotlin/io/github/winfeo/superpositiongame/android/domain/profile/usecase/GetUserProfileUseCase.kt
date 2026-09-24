package io.github.winfeo.superpositiongame.android.domain.profile.usecase

import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.github.winfeo.superpositiongame.android.domain.profile.ProfileRepository

class GetUserProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: Long): Result<AuthorizedUser> {
        if (userId < 0) {
            return Result.failure(IllegalArgumentException("Неверный id пользователя"))
        }

        return repository.getUserProfile(userId)
    }
}
