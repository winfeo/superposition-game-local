package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorizedUserDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.UpdateUserDTO
import io.github.winfeo.superpositiongame.android.data.source.NetworkConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserApi(
    private val client: HttpClient
) {
    private val REST_URL = NetworkConfig.REST_BASE_URL

    suspend fun getUserById(userId: Long): AuthorizedUserDTO {
        return client.get("$REST_URL/api/users/$userId").body()
    }

    suspend fun updateUser(updateUserDTO: UpdateUserDTO): AuthorizedUserDTO {
        return client.put("$REST_URL/api/users") {
            contentType(ContentType.Application.Json)
            setBody(updateUserDTO)
        }.body()
    }

    suspend fun deleteUser(userId: Long) {
        client.delete("$REST_URL/api/users/$userId")
    }

    suspend fun getCurrentUser(): AuthorizedUserDTO {
        return client.get("$REST_URL/api/users/me").body()
    }
}
