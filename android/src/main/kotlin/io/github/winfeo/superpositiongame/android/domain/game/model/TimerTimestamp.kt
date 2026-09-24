package io.github.winfeo.superpositiongame.android.domain.game.model

data class TimerTimestamp(
    val turnNumber: Int,
    val revision: Long,
    val timeLeftAtReceiptMs: Long,
    val receivedAtRealtimeMs: Long
)
