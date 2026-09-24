package io.github.winfeo.superpositiongame.android.data.dto.move

import kotlinx.serialization.Serializable

@Serializable
data class MoveCommandDTO(
    val expectedTurnNumber: Int,
    val move: MoveDTO
)
