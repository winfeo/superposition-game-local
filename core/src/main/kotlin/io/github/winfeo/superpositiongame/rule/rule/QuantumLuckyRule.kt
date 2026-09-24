package io.github.winfeo.superpositiongame.rule.rule

import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult
import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates

class QuantumLuckyRule : Rule {
    override fun check(ruleContext: RuleContext): ValidationResult? {
        if (ruleContext.card.type != CardType.QUANTUM_LUCKY) return null

        val slot = ruleContext.targetSlot
        if (slot.slotOwner != SlotOwner.PLAYER) {
            return rejected("Карту можно использовать только на своём слоте")
        }

        val requiredState = slot.dice.requiredState
            ?: return rejected("Для слота не задано целевое состояние")

        if (slot.dice.state == requiredState) {
            return rejected("Слот уже находится в требуемом состоянии")
        }

        return null
    }

    private fun rejected(message: String) = ValidationResult(
        canDrop = false,
        message = message,
        activeState = SlotActorStates.HOVERED_CANT_PLACE
    )
}
