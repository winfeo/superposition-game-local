package io.github.winfeo.superpositiongame.android.data.dto.state

import kotlinx.serialization.Serializable

@Serializable
data class CardDTO(
    val id: String,
    val type: String
)
