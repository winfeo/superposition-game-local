package io.github.winfeo.superpositiongame.model.card.description

import io.github.winfeo.superpositiongame.model.card.CardType

abstract class CardDescription {
    abstract val type: CardType
    abstract val axis: AxisRotation?
    //abstract val angle: AngleRotation?
    abstract val actionRadius: Int ///TODO заменить на энам
    abstract val requiredSpecialSlot: Boolean
    abstract val isForwardRotation: Boolean?
}
