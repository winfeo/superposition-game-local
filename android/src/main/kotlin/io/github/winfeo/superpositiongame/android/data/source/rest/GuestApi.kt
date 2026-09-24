package io.github.winfeo.superpositiongame.android.data.source.rest

import io.github.winfeo.superpositiongame.android.data.dto.rest.GuestResponse
import io.github.winfeo.superpositiongame.android.data.source.NetworkConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post

class GuestApi(
    private val client: HttpClient
) {
    private val REST_URL = NetworkConfig.REST_BASE_URL

    suspend fun createGuest(): GuestResponse {
        return client.post("$REST_URL/api/guest/create").body()
    }
}
