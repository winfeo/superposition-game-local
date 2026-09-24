package io.github.winfeo.superpositiongame.model.game

import io.github.winfeo.superpositiongame.model.card.Card

data class PlayerState(
    val id: String,
    val nickname: String?,
    val hand: List<Card> = emptyList(),
    val slots: List<SlotState> = emptyList(),
    val skipNextTurn: Boolean = false,
    val remainingMoves: Int = 1
)
