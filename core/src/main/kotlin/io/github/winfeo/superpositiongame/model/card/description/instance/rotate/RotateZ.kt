package io.github.winfeo.superpositiongame.model.card.description.instance.rotate

import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.card.description.CardDescription

data class RotateZ(
    override val type: CardType = CardType.ROTATE,
    override val axis: AxisRotation? = AxisRotation.Z,
    //override val angle: AngleRotation? = AngleRotation,
    override val actionRadius: Int = 1,
    override val requiredSpecialSlot: Boolean = false,
    override val isForwardRotation: Boolean? = true
): CardDescription()
