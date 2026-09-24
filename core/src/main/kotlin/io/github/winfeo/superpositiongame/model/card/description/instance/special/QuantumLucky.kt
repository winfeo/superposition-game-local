package io.github.winfeo.superpositiongame.model.card.description.instance.special

import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.card.description.CardDescription

data class QuantumLucky(
    override val type: CardType = CardType.QUANTUM_LUCKY,
    override val axis: AxisRotation? = null,
    override val actionRadius: Int = 1,
    override val requiredSpecialSlot: Boolean = false,
    override val isForwardRotation: Boolean? = null
) : CardDescription()
