package io.github.winfeo.superpositiongame.model.dice

import io.github.winfeo.superpositiongame.manager.DiceAtlasManager

object DiceFactory {
    private var idCounter = 0

    fun createRandomDice(): Dice {

        val state = DiceAtlasManager.getRandomDiceState()

        return Dice(
            id = "${state.stateName}_${idCounter++}",
            state = state,
            requiredState = DiceState.PLUS //TODO пока условие победы - кубиты ("++++")
        )
    }

    fun createDiceFromStates(diceState: DiceState, requiredState: DiceState): Dice {
        return Dice(
            id = "${diceState.stateName}_${idCounter++}",
            state = diceState,
            requiredState = requiredState
        )
    }

    fun getRandomDiceState(): DiceState = DiceState.entries.random()
    fun getRequiredDiceState(): DiceState = DiceState.PLUS ///TODO пока условие победы - кубиты ("++++")
}
