package io.github.winfeo.superpositiongame.android.data.dto.rest

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDTO(
    val id: Long,
    val nickname: String,
    val email: String
)
