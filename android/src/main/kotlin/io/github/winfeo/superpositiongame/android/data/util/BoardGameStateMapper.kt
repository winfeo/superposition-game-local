package io.github.winfeo.superpositiongame.android.data.util

import io.github.winfeo.superpositiongame.android.domain.game.model.BoardSession
import io.github.winfeo.superpositiongame.model.card.CardFactory
import io.github.winfeo.superpositiongame.model.card.CardTypeNew
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GamePhase
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.PlayerState
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.model.game.SlotState

class BoardGameStateMapper {
    fun map(session: BoardSession): GameState? {
        val board = session.game?: return null
        val selfId = session.selfId?: return null

        if (board.playerIds.size != 2 || selfId !in board.playerIds ||
            board.registers.size != 2 || board.registers.any { it.size != 4 }
        ) return null

        val players = board.playerIds.mapIndexed { playerIndex, id ->
            val owner = if (id == selfId) SlotOwner.PLAYER else SlotOwner.OPPONENT
            val slots = board.registers[playerIndex].mapIndexed { slotIndex, value ->
                val diceState = value.toDiceState()?: return null
                val dice = Dice(id = "$id-$slotIndex", state = diceState)
                val cards = board.cardsOnField
                    .filter { it.player == playerIndex && it.cubit == slotIndex }
                    .mapNotNull { field ->
                        field.card.toCardType()?.let { type ->
                            CardFactory.buildCardFromType(type, "$id-$slotIndex-${field.card}")
                        }
                    }
                SlotState(slotIndex, owner, dice, dice, cards)
            }
            id.toString() to PlayerState(
                id = id.toString(),
                nickname = session.players.firstOrNull { it.id == id }?.name ?: "player$id",
                slots = slots
            )
        }.toMap()

        return GameState(
            phase = if (session.winner == null) GamePhase.MOVE_START else GamePhase.GAME_FINISHED,
            currentPlayerId = board.currentPlayerId?.toString() ?: return null,
            players = players,
            turnNumber = board.playedCards,
            activeSlotsRow = null,
            winnerId = session.winner?.id?.toString(),
            serverTime = 0L,
            turnEndsAt = 0L
        )
    }
}

private fun String.toDiceState(): DiceState? = when (lowercase()) {
    "0" -> DiceState.ZERO
    "1" -> DiceState.ONE
    "+" -> DiceState.PLUS
    "-" -> DiceState.MINUS
    "i", "+i" -> DiceState.I
    "-i" -> DiceState.I_MINUS
    else -> null
}

private fun String.toCardType(): CardTypeNew? = when (lowercase()) {
    "pauli_x" -> CardTypeNew.PAULI_X
    "pauli_x3" -> CardTypeNew.PAULI_X_3
    "pauli_y" -> CardTypeNew.PAULI_Y
    "pauli_y3" -> CardTypeNew.PAULI_Y_3
    "pauli_z" -> CardTypeNew.PAULI_Z
    "pauli_z3" -> CardTypeNew.PAULI_Z_3
    "hadamard", "hadamard_h" -> CardTypeNew.HADAMARD
    "hadamard_3", "hadamard_h3" -> CardTypeNew.HADAMARD_3
    "rotate_x" -> CardTypeNew.ROTATE_X
    "rotate_y" -> CardTypeNew.ROTATE_Y
    "rotate_z" -> CardTypeNew.ROTATE_Z
    "phase_s" -> CardTypeNew.PHASE_FORWARD
    "phase_s_backwards" -> CardTypeNew.PHASE_BACKWARD
    "identity" -> CardTypeNew.IDENTITY
    "measurement" -> CardTypeNew.MEASUREMENT
    "kronecker_multiplication" -> CardTypeNew.KRONECKER_MULTIPLICATION
    "quantum_noise" -> CardTypeNew.QUANTUM_NOISE
    "quantum_lucky" -> CardTypeNew.QUANTUM_LUCKY
    "swap" -> CardTypeNew.SWAP
    "reshuffle" -> CardTypeNew.RESHUFFLE
    else -> null
}
