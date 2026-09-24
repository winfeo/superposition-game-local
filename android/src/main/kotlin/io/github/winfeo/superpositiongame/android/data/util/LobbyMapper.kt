package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.data.dto.socket.LobbyResponseDTO
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Lobby
import io.github.winfeo.superpositiongame.android.domain.lobby.model.Player

fun LobbyResponseDTO.toDomain(): Lobby {
    return Lobby(
        players = players.map {
            Player(
                id = it.id,
                nickname = it.nickname
            )
        }
    )
}
