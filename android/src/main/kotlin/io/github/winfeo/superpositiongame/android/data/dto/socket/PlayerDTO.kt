package io.github.winfeo.superpositiongame.android.data.dto.socket

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDTO (
    val id: String,
    val nickname: String?
)
