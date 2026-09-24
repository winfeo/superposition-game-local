package io.github.winfeo.superpositiongame.android.data.dto.rest

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequestDTO (
    val email: String,
    val password: String
)
