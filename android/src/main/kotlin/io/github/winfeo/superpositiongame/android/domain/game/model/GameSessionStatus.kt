package io.github.winfeo.superpositiongame.android.domain.game.model

enum class GameSessionStatus {
    WAITING_FOR_PLAYERS,
    ACTIVE,
    PAUSED_FOR_RECONNECT,
    FINISHED,
    CANCELLED,
    UNKNOWN
}
