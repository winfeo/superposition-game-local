package io.github.winfeo.superpositiongame.android.domain.game.model

data class GameLifecycleEvent(
    val gameId: String,
    val status: GameSessionStatus,
    val disconnectedPlayerIds: Set<String>,
    val reconnectDeadlines: Map<String, Long>,
    val serverTime: Long,
    val winnerId: String?,
    val endReason: GameEndReason?
)
