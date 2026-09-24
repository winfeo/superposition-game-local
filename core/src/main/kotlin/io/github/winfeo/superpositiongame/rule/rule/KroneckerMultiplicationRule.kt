package io.github.winfeo.superpositiongame.rule.rule

import com.badlogic.gdx.Gdx
import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult

//Правило: использование только одного регистра (после применения Kronecker Multiplication)
class KroneckerMultiplicationRule: Rule {
    override fun check(ruleContext: RuleContext): ValidationResult? {
        Gdx.app.log("MULTI", "Владелец слота: ${ruleContext.targetSlot.slotOwner}" +
            "Активные слоты: ${ruleContext.activeSlotsRow}")
        val activeSlotsRow = ruleContext.activeSlotsRow?: return null

        val slot = ruleContext.targetSlot
        if (slot.slotOwner != activeSlotsRow) {
            return ValidationResult(
                canDrop = false,
                message = """
                    Невозможно использовать карту (другой регистр)
                    Активные слоты: $activeSlotsRow
                    Слот: ${slot.slotOwner}
                """.trimIndent(),

                activeState = SlotActorStates.HOVERED_CANT_PLACE
            )
        }

        return null
    }
}
