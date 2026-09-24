package io.github.winfeo.superpositiongame.rule.model

import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates

//Результат проверки правил
data class ValidationResult(
    val canDrop: Boolean,
    val message: String? = null,
    //val activeColor: Color? = null
    val activeState: SlotActorStates = SlotActorStates.NO_ACTION
)
