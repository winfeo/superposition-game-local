package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.UpdateUserDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.GameHistoryApi
import io.github.winfeo.superpositiongame.android.data.source.rest.UserApi
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.github.winfeo.superpositiongame.android.domain.history.GameHistoryItem
import io.github.winfeo.superpositiongame.android.domain.profile.ProfileRepository

class ProfileRepositoryImpl(
    private val userApi: UserApi,
    private val gameHistoryApi: GameHistoryApi
): ProfileRepository {
    override suspend fun getUserProfile(userId: Long): Result<AuthorizedUser> {
        return try {
            val dto = userApi.getUserById(userId)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNickname(
        userId: Long,
        newNickname: String
    ): Result<AuthorizedUser> {
        return try {
            val currentUser = userApi.getUserById(userId)
            val updateDto = UpdateUserDTO(
                id = userId,
                nickname = newNickname,
                email = currentUser.email
            )
            val updatedDto = userApi.updateUser(updateDto)
            Result.success(updatedDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGameHistory(userId: Long): Result<List<GameHistoryItem>> {
        return try {
            val historyDto = gameHistoryApi.getGameHistory(userId)
            val domainList = historyDto.map { it.toDomain() }
            Result.success(domainList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
