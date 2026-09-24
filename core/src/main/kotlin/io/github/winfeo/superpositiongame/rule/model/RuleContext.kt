package io.github.winfeo.superpositiongame.rule.model

import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.model.game.SlotState
import io.github.winfeo.superpositiongame.model.card.Card

//Необходимый контекст для проверки правил
data class RuleContext(
    val card: Card,
    val targetSlot: SlotState,
    val playerSlots: List<SlotState>,
    val opponentSlots: List<SlotState>,
    val activeSlotsRow: SlotOwner?
)
