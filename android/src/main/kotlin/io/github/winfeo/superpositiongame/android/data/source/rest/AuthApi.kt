package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthRequestDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthResponseDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.AuthorizedUserDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.NewUserDTO
import io.github.winfeo.superpositiongame.android.data.source.NetworkConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthApi(
    private val client: HttpClient
) {
    private val REST_URL = NetworkConfig.REST_BASE_URL

    suspend fun register(dto: NewUserDTO): AuthorizedUserDTO {
        val response = client.post("$REST_URL/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess()) {
            throw Exception(response.bodyAsText())
        }

        return response.body()
    }

    suspend fun login(dto: AuthRequestDTO): AuthResponseDTO {
        val response = client.post("$REST_URL/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess()) {
            throw Exception(response.bodyAsText())
        }

        return response.body()
    }
}
