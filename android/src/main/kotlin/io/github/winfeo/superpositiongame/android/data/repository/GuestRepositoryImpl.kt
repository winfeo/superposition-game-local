package io.github.winfeo.superpositiongame.android.data.repository

import io.github.winfeo.superpositiongame.android.data.source.rest.GuestApi

class GuestRepositoryImpl(
    private val api: GuestApi
) {
    suspend fun createGuest(): Result<String> {
        return try {
            val response = api.createGuest()
            Result.success(response.guestId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
