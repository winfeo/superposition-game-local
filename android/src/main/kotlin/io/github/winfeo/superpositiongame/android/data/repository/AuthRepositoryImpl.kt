package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthRequestDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.NewUserDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.AuthApi
import io.github.winfeo.superpositiongame.android.data.util.toDomain
import io.github.winfeo.superpositiongame.android.domain.auth.AuthRepository
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthToken
import io.github.winfeo.superpositiongame.android.domain.auth.model.AuthorizedUser
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

class AuthRepositoryImpl(
    private val authApi: AuthApi
): AuthRepository {
    override suspend fun register(email: String, password: String): Result<AuthorizedUser> {
        return try {
            val userDto = authApi.register(NewUserDTO(email, password))
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(Exception(extractErrorMessage(e)))
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Pair<AuthorizedUser, AuthToken>> {
        return try {
            val response = authApi.login(AuthRequestDTO(email, password))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(Exception(extractErrorMessage(e)))
        }
    }

    private suspend fun extractErrorMessage(e: Exception): String {
        return when (e) {
            is ClientRequestException -> {
                try { e.response.bodyAsText() }
                catch (_: Exception) { "Ошибка запроса" }
            }
            is ServerResponseException -> {
                try { e.response.bodyAsText() }
                catch (_: Exception) { "Ошибка сервера" }
            }
            is ConnectException -> "Нет подключения к серверу"
            is UnknownHostException -> "Нет подключения к серверу"
            is IOException -> "Нет подключения к серверу"
            else -> "${e.message}"
        }
    }
}
