package io.github.winfeo.superpositiongame.android.domain.auth.model

data class AuthorizedUser(
    val id: Long,
    val email: String,
    val league: String,
    val nickname: String,
    val ratingPoints: Int,
    val winsAmount: Int,
    val gamesPlayed: Int,
    val createdAt: String
)
