package io.github.winfeo.superpositiongame.rule.rule

import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.Dice
import io.github.winfeo.superpositiongame.model.dice.DiceState

class RotateCompatibilityRule {
    companion object { //TODO вынести в утилиту?
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

    fun check(
        dice: Dice,
        card: Card
    ): Boolean {
        return isCardCompatibleWithArrow(card, dice)
    }

    fun isCardCompatibleWithArrow(card: Card, dice: Dice): Boolean {
        val cardAxis = card.axis
        val diceState = dice.state

        return axisCompatibility[cardAxis]?.contains(diceState)?: false
    }

}
