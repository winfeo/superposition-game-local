package io.github.winfeo.superpositiongame.model.game

import io.github.winfeo.superpositiongame.model.dice.DiceState

//все команды, которые меняют состояние игры
//действия игрока в игре
sealed class Move {
    abstract val playerId: String
    abstract val type: GameMoveType
    abstract val expectedTurnNumber: Int
    //TODO вынести cardId сюда тоже

    data class PlayCard(
        override val type: GameMoveType = GameMoveType.PLAY_CARD,
        override val playerId: String,
        override val expectedTurnNumber: Int,
        val cardId: String,
        val targetSlotIndex: Int,
        val targetPlayerId: String
    ): Move()

    data class RotateDice(
        override val type: GameMoveType = GameMoveType.ROTATE_DICE,
        override val playerId: String,
        override val expectedTurnNumber: Int,
        val cardId: String,
        val targetSlotIndex: Int,
        val newState: DiceState,
        val targetPlayerId: String
    ): Move()

    data class SwapDices(
        override val type: GameMoveType = GameMoveType.SWAP_DICES,
        override val playerId: String,
        override val expectedTurnNumber: Int,
        val cardId: String,
        val firstSlotIndex: Int,
        val secondSlotIndex: Int,
        val firstSlotOwner: String,
        val secondSlotOwner: String
    ): Move()

    data class DoubleTapEffect(
        override val type: GameMoveType = GameMoveType.DOUBLE_TAP,
        override val playerId: String,
        override val expectedTurnNumber: Int,
        val cardId: String
    ): Move()

    data class ReshuffleCard(
        override val type: GameMoveType = GameMoveType.RESHUFFLE_CARD,
        override val playerId: String,
        override val expectedTurnNumber: Int,
        val cardId: String,
        val cardsToChange: List<String>
    ): Move()

    data class Surrender(
        override val type: GameMoveType = GameMoveType.SURRENDER,
        override val playerId: String,
        override val expectedTurnNumber: Int
    ): Move()
}
