package io.github.winfeo.superpositiongame.android.data.dto.rest

import kotlinx.serialization.Serializable

@Serializable
data class NewUserDTO(
    val email: String,
    val password: String
)
