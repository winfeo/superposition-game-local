package io.github.winfeo.superpositiongame.rule.rule

import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult

//Правило: является ли слот граничным (при использовании x-3 карт)
class TripleEffectBorderRule: Rule {
    override fun check(ruleContext: RuleContext): ValidationResult? {
        val card = ruleContext.card
        val slot = ruleContext.targetSlot

        if (card.actionRadius == 3) {
            val slots = when (slot.slotOwner) {
                SlotOwner.PLAYER -> ruleContext.playerSlots
                SlotOwner.OPPONENT -> ruleContext.opponentSlots
            }

            if (slot.index == 0 || slot.index == slots.lastIndex) {
                return ValidationResult(
                    canDrop = false,
                    message = "Невозможно использовать карту (крайний слот)",
                    activeState = SlotActorStates.HOVERED_CANT_PLACE
                )
            }
        }

        return null
    }
}
