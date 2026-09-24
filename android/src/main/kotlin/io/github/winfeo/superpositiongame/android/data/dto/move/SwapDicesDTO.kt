package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SWAP_DICES")
data class SwapDicesDTO(
    override val playerId: String,
    val cardId: String,
    val firstSlotIndex: Int,
    val secondSlotIndex: Int,
    val firstSlotOwner: String,
    val secondSlotOwner: String
): MoveDTO()
