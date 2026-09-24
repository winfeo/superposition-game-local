package io.github.winfeo.superpositiongame.model.dice

data class Dice(
    val id: String,
    var state: DiceState,
    val requiredState: DiceState? = null
)
