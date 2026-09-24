package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorizedUserDTO
import io.github.winfeo.superpositiongame.android.data.source.rest.UserApi
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

class UserRepository(
    private val api: UserApi
) {
    suspend fun getCurrentUser(): Result<AuthorizedUserDTO> {
        return try {
            val user = api.getCurrentUser()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
