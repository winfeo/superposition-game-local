package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.CreateAiGameRequestDTO
import io.github.winfeo.superpositiongame.android.data.source.local.UserSession
import io.github.winfeo.superpositiongame.android.data.source.rest.AiGameApi
import io.github.winfeo.superpositiongame.android.domain.ai.AiGameRepository
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiDifficulty
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiGameError
import io.github.winfeo.superpositiongame.android.domain.ai.model.AiGameException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import java.io.IOException

class AiGameRepositoryImpl(
    private val api: AiGameApi
) : AiGameRepository {
    override suspend fun createGame(difficulty: AiDifficulty): Result<String> {
        val playerId = UserSession.currentUserId.value
        val isAuthorized = UserSession.isAuthorized.value

        if (playerId.isNullOrBlank() || (isAuthorized && UserSession.token.value.isNullOrBlank())) {
            return Result.failure(AiGameException(AiGameError.SESSION_UNAVAILABLE))
        }

        val guestId = if (isAuthorized) null else playerId
        if (guestId != null && !guestId.startsWith("guest-")) {
            return Result.failure(AiGameException(AiGameError.INVALID_SESSION))
        }

        return try {
            val response = api.createGame(
                CreateAiGameRequestDTO(
                    difficulty = difficulty.name,
                    guestId = guestId
                )
            )

            if (response.gameId.isBlank()) {
                Result.failure(AiGameException(AiGameError.UNKNOWN))
            } else {
                Result.success(response.gameId)
            }

        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: ResponseException) {
            val error = when (exception.response.status) {
                HttpStatusCode.BadRequest -> AiGameError.INVALID_SESSION
                HttpStatusCode.Unauthorized -> AiGameError.SESSION_UNAVAILABLE
                HttpStatusCode.Conflict -> AiGameError.ALREADY_IN_GAME
                else -> AiGameError.SERVER_UNAVAILABLE
            }

            Result.failure(AiGameException(error, exception))
        } catch (exception: IOException) {
            Result.failure(AiGameException(AiGameError.NETWORK_ERROR, exception))
        } catch (exception: Exception) {
            Result.failure(AiGameException(AiGameError.UNKNOWN, exception))
        }
    }
}
