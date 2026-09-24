package io.github.winfeo.superpositiongame.rule

import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates
import io.github.winfeo.superpositiongame.rule.model.ValidationResult
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.rule.*

//Проверка игровых правил
///TODO вынести на сервер тоже?
object RuleEngine {
    private val rules = listOf(
        FrozenSlotRule(),
        QuantumNoiseRule(),
        QuantumLuckyRule(),
//        ArrowCompatibilityRule(),
        TripleEffectBorderRule(),
        KroneckerMultiplicationRule()
    )
    fun checkRules(ruleContext: RuleContext): ValidationResult {
        for (rule in rules) {
            val result = rule.check(ruleContext)

            if (result != null) {
                return result
            }
        }

        return ValidationResult(
            canDrop = true,
            message = null,
            activeState = SlotActorStates.HOVERED_CAN_PLACE
        )
    }
}
