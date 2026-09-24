package io.github.winfeo.superpositiongame.model.game

import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.Dice

data class SlotState(
    val index: Int,
    val slotOwner: SlotOwner,
    val initialDice: Dice,
    val dice: Dice,
    val appliedCards: List<Card> = emptyList(),
    val isFrozen: Boolean = false
)
