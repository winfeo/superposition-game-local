package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.data.dto.socket.TimerUpdatePacketDTO
import io.github.winfeo.superpositiongame.android.domain.game.model.TimerUpdatePacket

fun TimerUpdatePacketDTO.toDomain(): TimerUpdatePacket {
    return TimerUpdatePacket(
        turnNumber = turnNumber,
        timeLeftMs = timeLeftMs,
        revision = revision
    )
}
