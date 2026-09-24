package io.github.winfeo.superpositiongame.android.domain.history

data class GameHistoryItem(
    val isWinner: Boolean,
    val opponentNickname: String,
    val totalMoves: Int,
    val ratingChange: Int,
    val playedAt: String
)
