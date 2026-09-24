package io.github.winfeo.superpositiongame.android.data.dto.rest

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizedUserDTO(
    val id: Long,
    val email: String,
    val league: String,
    val nickname: String,
    val ratingPoints: Int,
    val winsAmount: Int,
    val gamesPlayed: Int,
    val createdAt: String
)
