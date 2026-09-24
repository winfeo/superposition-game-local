package io.github.winfeo.superpositiongame.android.data.dto.state

import kotlinx.serialization.Serializable

@Serializable
data class GameStateDTO(
    val phase: String,
    val currentPlayerId: String,
    val players: Map<String, PlayerStateDTO>, ///TODO не хранить обоих игроков, хранить только стейт самого игрока
    val turnNumber: Int,
    val activeSlotsRow: String?, //переименовать энам? ///TODO sealed class с эффектами сделать?
    val winnerId: String?,
    val serverTime: Long?,
    val turnEndsAt: Long?
)
