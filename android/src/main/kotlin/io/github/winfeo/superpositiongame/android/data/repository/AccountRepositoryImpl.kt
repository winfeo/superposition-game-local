package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.UpdateUserDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.UserApi
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.github.winfeo.superpositiongame.android.domain.settings.AccountRepository

class AccountRepositoryImpl(
    private val userApi: UserApi
): AccountRepository {
    override suspend fun updateEmail(
        userId: Long,
        newEmail: String
    ): Result<AuthorizedUser> {
        return try {
            val currentUser = userApi.getUserById(userId)
            val updateDto = UpdateUserDTO(
                id = userId,
                nickname = currentUser.nickname,
                email = newEmail
            )

            val updatedDto = userApi.updateUser(updateDto)
            Result.success(updatedDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(userId: Long): Result<Unit> {
        return try {
            userApi.deleteUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
