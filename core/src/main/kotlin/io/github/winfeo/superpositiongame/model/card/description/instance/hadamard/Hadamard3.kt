package io.github.winfeo.superpositiongame.model.card.description.instance.hadamard

import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.card.description.CardDescription

data class Hadamard3(
    override val type: CardType = CardType.HADAMARD,
    override val axis: AxisRotation? = AxisRotation.Y,
    //override val angle: AngleRotation? = AngleRotation,
    override val actionRadius: Int = 3,
    override val requiredSpecialSlot: Boolean = true,
    override val isForwardRotation: Boolean? = true
): CardDescription()
