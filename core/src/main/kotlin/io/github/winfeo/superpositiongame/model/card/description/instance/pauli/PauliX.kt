package io.github.winfeo.superpositiongame.model.card.description.instance.pauli

import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.card.description.CardDescription

data class PauliX(
    override val type: CardType = CardType.PAULI,
    override val axis: AxisRotation? = AxisRotation.X,
    //override val angle: AngleRotation? = AngleRotation,
    override val actionRadius: Int = 1,
    override val requiredSpecialSlot: Boolean = false,
    override val isForwardRotation: Boolean? = true
): CardDescription()
