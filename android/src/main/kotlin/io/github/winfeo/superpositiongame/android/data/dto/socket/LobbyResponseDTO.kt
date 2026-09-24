package io.github.winfeo.superpositiongame.android.data.dto.socket

import io.github.winfeo.superpositiongame.android.data.dto.socket.PlayerDTO
import kotlinx.serialization.Serializable

@Serializable
data class LobbyResponseDTO(
    val players: List<PlayerDTO>
)
