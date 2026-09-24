package io.github.winfeo.superpositiongame.android.data.dto.state

import kotlinx.serialization.Serializable

@Serializable
data class PlayerStateDTO(
    val id: String,
    val nickname: String?,
    val hand: List<CardDTO> = emptyList(),
    val slots: List<SlotStateDTO> = emptyList(),
    val skipNextTurn: Boolean = false,
    val remainingMoves: Int = 1
)
