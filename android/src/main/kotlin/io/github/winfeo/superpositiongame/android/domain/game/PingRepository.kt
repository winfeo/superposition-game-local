package io.github.winfeo.superpositiongame.android.domain.game

import io.github.winfeo.superpositiongame.android.domain.game.model.TimerUpdatePacket
import kotlinx.coroutines.flow.Flow

interface PingRepository {
    suspend fun measureRTT(): Long
}
