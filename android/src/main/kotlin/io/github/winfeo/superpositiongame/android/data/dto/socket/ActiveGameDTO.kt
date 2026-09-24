package io.github.winfeo.superpositiongame.android.data.dto.socket

import kotlinx.serialization.Serializable

@Serializable
data class ActiveGameDTO(
    val gameId: String,
    val status: String,
    val opponentId: String,
    val opponentNickname: String? = null,
    val currentPlayerDisconnected: Boolean = false,
    val reconnectDeadline: Long? = null,
    val serverTime: Long = 0L
)
