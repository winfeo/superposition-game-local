package io.github.winfeo.superpositiongame.android.data.dto.rest

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDTO(
    val token: String,
    val user: AuthorizedUserDTO
)
