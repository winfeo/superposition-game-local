package io.github.winfeo.superpositiongame.rule.rule

import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult

//Правило: можно ли отменить состояние Quantum Noise
class QuantumNoiseRule: Rule {
    override fun check(ruleContext: RuleContext): ValidationResult? {
        val slot = ruleContext.targetSlot
        val card = ruleContext.card

        if (card.type != CardType.QUANTUM_NOISE) return null

        if (slot.appliedCards.isEmpty()) {
            return returnResult(message = "Невозможно использовать карту (слот пустой)")
        }

        val lastCard = slot.appliedCards.last()
        if (
            lastCard.actionRadius == 3 ||
            lastCard.type == CardType.ROTATE ||
            lastCard.type == CardType.QUANTUM_LUCKY
        ) {
            return returnResult(message = "Невозможно использовать на этот тип карты")
        }

        return null
    }

    private fun returnResult(message: String): ValidationResult {
        return ValidationResult(
            canDrop = false,
            message = message,
            activeState = SlotActorStates.HOVERED_CANT_PLACE
        )
    }
}
