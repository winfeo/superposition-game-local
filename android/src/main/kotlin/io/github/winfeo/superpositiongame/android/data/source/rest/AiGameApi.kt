package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.CreateAiGameRequestDTO
import io.github.winfeo.superpositiongame.android.data.dto.rest.CreateAiGameResponseDTO
import io.github.winfeo.superpositiongame.android.data.source.NetworkConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AiGameApi(
    private val client: HttpClient
) {
    private val REST_URL = NetworkConfig.REST_BASE_URL

    suspend fun createGame(request: CreateAiGameRequestDTO): CreateAiGameResponseDTO {
        return client.post("$REST_URL/api/games/ai") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
