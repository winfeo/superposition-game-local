package io.github.winfeo.superpositiongame.android.data.dto.rest

import kotlinx.serialization.Serializable

@Serializable
data class GameHistoryDTO(
    val isWinner: Boolean,
    val opponentNickname: String,
    val totalMoves: Int,
    val ratingChange: Int,
    val playedAt: String
)
