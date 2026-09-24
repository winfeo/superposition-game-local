package io.github.winfeo.superpositiongame.android.data.dto.state

import kotlinx.serialization.Serializable

@Serializable
data class SlotStateDTO(
    val index: Int,
    val ownerId: String,
    val initialDice: DiceDTO,
    val dice: DiceDTO,
    val appliedCards: List<CardDTO> = emptyList(),
    val isFrozen: Boolean = false
)

