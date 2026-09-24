package io.github.winfeo.superpositiongame.android.data.dto.socket

import kotlinx.serialization.Serializable

@Serializable
data class ActiveGameResponseDTO(
    val hasActiveGame: Boolean,
    val game: ActiveGameDTO? = null
)
