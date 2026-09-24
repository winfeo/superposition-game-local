package io.github.winfeo.superpositiongame.android.data.dto.rest

import kotlinx.serialization.Serializable

@Serializable
data class CreateAiGameResponseDTO(
    val gameId: String,
    val aiPlayerId: String,
    val difficulty: String
)
