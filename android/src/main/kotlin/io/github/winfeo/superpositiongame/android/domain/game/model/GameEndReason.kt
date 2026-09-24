package io.github.winfeo.superpositiongame.android.domain.game.model

enum class GameEndReason {
    OBJECTIVE_COMPLETED,
    SURRENDER,
    RECONNECT_DECLINED,
    RECONNECT_TIMEOUT,
    BOTH_PLAYERS_DISCONNECTED,
    UNKNOWN
}
