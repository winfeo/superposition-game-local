package io.github.winfeo.superpositiongame.android.data.dto.state

import kotlinx.serialization.Serializable

@Serializable
data class DiceDTO(
    val id: String,
    var state: String,
    val requiredState: String? = null
)

