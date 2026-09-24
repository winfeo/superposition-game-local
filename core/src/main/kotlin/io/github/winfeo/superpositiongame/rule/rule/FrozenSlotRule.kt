package io.github.winfeo.superpositiongame.rule.rule

import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult

//Правило: заморожен слот или нет
class FrozenSlotRule: Rule {
    override fun check(ruleContext: RuleContext): ValidationResult? {
        val slot = ruleContext.targetSlot
        val card = ruleContext.card

        if (
            slot.isFrozen &&
            card.type !in listOf(CardType.SWAP, CardType.QUANTUM_NOISE, CardType.MEASUREMENT)
            ) {
            return ValidationResult(
                canDrop = false,
                message = "Невозможно использовать карту (слот заморожен)",
                activeState = SlotActorStates.HOVERED_CANT_PLACE
            )
        }

        return null
    }
}
