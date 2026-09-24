package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ROTATE_DICE")
data class RotateDiceDTO(
    override val playerId: String,
    val cardId: String,
    val targetSlotIndex: Int,
    val newState: String,
    val targetPlayerId: String
): MoveDTO()
