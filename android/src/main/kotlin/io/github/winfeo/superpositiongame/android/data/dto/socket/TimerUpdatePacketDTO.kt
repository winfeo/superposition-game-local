package io.github.winfeo.superpositiongame.android.data.dto.socket

import kotlinx.serialization.Serializable

@Serializable
data class TimerUpdatePacketDTO(
    val turnNumber: Int,
    val timeLeftMs: Long,
    val revision: Long
) {
}
