package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PLAY_CARD")
data class PlayCardDTO(
    override val playerId: String,
    val cardId: String,
    val targetSlotIndex: Int,
    val targetPlayerId: String,
): MoveDTO()
