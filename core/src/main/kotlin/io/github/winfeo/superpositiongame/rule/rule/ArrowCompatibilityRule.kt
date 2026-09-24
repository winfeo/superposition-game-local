package io.github.winfeo.superpositiongame.rule.rule

import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult
import kotlin.collections.setOf

//Правило: совместимость стрелок на кубите и карте (совместимость осей)
class ArrowCompatibilityRule: Rule {
    companion object {
        private val axisCompatibility = mapOf(
            AxisRotation.X to setOf(
                DiceState.ZERO,
                DiceState.ONE,
                DiceState.I,
                DiceState.I_MINUS
            ),
            AxisRotation.Y to setOf(
                DiceState.ZERO,
                DiceState.ONE,
                DiceState.PLUS,
                DiceState.MINUS
            ),
            AxisRotation.Z to setOf(
                DiceState.PLUS,
                DiceState.MINUS,
                DiceState.I,
                DiceState.I_MINUS
            )
        )
    }

    override fun check(ruleContext: RuleContext): ValidationResult? {
        val card = ruleContext.card
        val dice = ruleContext.targetSlot.dice

        val result = isCardCompatibleWithArrow(card, dice)
        if (!result) {
            return ValidationResult(
                canDrop = false,
                message = """
                    Невозможно использовать карту (несовместимая ось вращения)
                    Карта: ${card.axis}
                    Дайс: ${dice.state.name}
                """.trimIndent(),
                activeState = SlotActorStates.HOVERED_CANT_PLACE
            )
        }

        return null
    }

    ///TODO переделать (в утилиту?)
    fun isCardCompatibleWithArrow(card: Card, dice: Dice): Boolean {
        val cardAxis = card.axis
        val diceState = dice.state

        //Если специальная карта, то у неё нет оси вращения
        if (cardAxis == null) {
            return true
        }

        return axisCompatibility[cardAxis]?.contains(diceState)?: false
    }
}
