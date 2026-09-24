package io.github.winfeo.superpositiongame.android.domain.game.model

data class ActiveGame(
    val gameId: String,
    val status: GameSessionStatus,
    val opponentId: String,
    val opponentNickname: String?,
    val currentPlayerDisconnected: Boolean,
    val reconnectDeadline: Long?,
    val serverTime: Long
)
