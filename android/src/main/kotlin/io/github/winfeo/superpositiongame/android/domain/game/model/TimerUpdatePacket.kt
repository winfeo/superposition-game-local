package io.github.winfeo.superpositiongame.android.domain.game.model

data class TimerUpdatePacket(
    val turnNumber: Int,
    val timeLeftMs: Long,
    val revision: Long
)
