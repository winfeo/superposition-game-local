package io.github.winfeo.superpositiongame.android.data.dto.socket

import kotlinx.serialization.Serializable

@Serializable
data class GameLifecycleEventDTO(
    val gameId: String,
    val status: String,
    val disconnectedPlayerIds: Set<String> = emptySet(),
    val reconnectDeadlines: Map<String, Long> = emptyMap(),
    val serverTime: Long = 0L,
    val winnerId: String? = null,
    val endReason: String? = null
)
