package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.GameHistoryDTO
import io.github.winfeo.superpositiongame.android.data.source.NetworkConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class GameHistoryApi(
    private val client: HttpClient
) {
    private val REST_URL = NetworkConfig.REST_BASE_URL

    suspend fun getGameHistory(userId: Long): List<GameHistoryDTO> {
        return client.get("$REST_URL/api/history/$userId").body()
    }
}
