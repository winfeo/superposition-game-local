package io.github.winfeo.superpositiongame.model.game


//Состояние игры (снимок)
data class GameState(
    val phase: GamePhase,
    val currentPlayerId: String, //TODO поменять на activePlayerId потом
    val players: Map<String, PlayerState>, ///TODO не хранить обоих игроков, хранить только стейт самого игрока
    val turnNumber: Int,
    val activeSlotsRow: SlotOwner?, //переименовать энам? ///TODO sealed class с эффектами сделать?
    val winnerId: String?,
    val serverTime: Long,
    val turnEndsAt: Long
)
