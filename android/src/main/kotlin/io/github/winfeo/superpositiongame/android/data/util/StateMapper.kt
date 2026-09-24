package io.github.winfeo.superpositiongame.android.data.util

import android.util.Log
import io.github.winfeo.superpositiongame.android.data.dto.state.CardDTO
import io.github.winfeo.superpositiongame.android.data.dto.state.DiceDTO
import io.github.winfeo.superpositiongame.android.data.dto.state.GameStateDTO
import io.github.winfeo.superpositiongame.android.data.dto.state.PlayerStateDTO
import io.github.winfeo.superpositiongame.android.data.dto.state.SlotStateDTO
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardFactory
import io.github.winfeo.superpositiongame.model.card.CardTypeNew
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GamePhase
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.PlayerState
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.model.game.SlotState

fun GameStateDTO.toDomain(playerId: String): GameState {
    return GameState(
        phase = GamePhase.valueOf(this.phase),
        currentPlayerId = this.currentPlayerId,
        players = this.players.mapValues { (_, playerDto) ->
            playerDto.toDomain(playerId)
        },
        turnNumber = this.turnNumber,
        activeSlotsRow = slotOwnerOrNull(
            playerId = playerId,
            slotsId = this.activeSlotsRow
        ),
        winnerId = this.winnerId,
        serverTime = this.serverTime?: 0L,
        turnEndsAt = this.turnEndsAt?: 0L
    )
}

fun PlayerStateDTO.toDomain(playerId: String): PlayerState {
    return PlayerState(
        id = this.id,
        nickname = this.nickname,
        hand = this.hand.map { it.toDomain() },
        slots = this.slots.map { it.toDomain(playerId) },
        skipNextTurn = this.skipNextTurn,
        remainingMoves = this.remainingMoves
    )
}

fun SlotStateDTO.toDomain(playerId: String): SlotState {
    Log.d("DTO", "Id игрока: $playerId\n" +
        "Владелец слота: ${this.ownerId}"
    )
    return SlotState(
        index = this.index,
        slotOwner = if (this.ownerId == playerId) SlotOwner.PLAYER else SlotOwner.OPPONENT,
        initialDice = this.initialDice.toDomain(),
        dice = this.dice.toDomain(),
        appliedCards = this.appliedCards.map { it.toDomain() },
        isFrozen = this.isFrozen
    )
}

fun DiceDTO.toDomain(): Dice {
    return Dice(
        id = this.id,
        state = DiceState.valueOf(this.state),
        requiredState = this.requiredState?.let { DiceState.valueOf(it) }
    )
}

fun CardDTO.toDomain(): Card {
    val type = CardTypeNew.valueOf(this.type)
    val card = CardFactory.buildCardFromType(type, this.id)
    return card;
}

private fun slotOwnerOrNull(playerId: String, slotsId: String?): SlotOwner? {
    Log.d("GAME_SLOT", "Маппинг. PlayerId: $playerId" +
        "SlotId: $slotsId")
//    val slotOwner = try {
//        if (playerId == slotsId) SlotOwner.PLAYER else SlotOwner.OPPONENT
//    } catch (e: Exception) {
//        null
//    }

    if (slotsId.isNullOrEmpty()) return null
    val slotOwner = if (playerId == slotsId) SlotOwner.PLAYER else SlotOwner.OPPONENT

    Log.d("GAME_SLOT", "Слот овнер: $slotOwner")
    return slotOwner
}
