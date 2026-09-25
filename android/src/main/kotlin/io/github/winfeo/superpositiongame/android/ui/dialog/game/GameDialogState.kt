package io.github.winfeo.superpositiongame.android.ui.dialog.game

import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState

sealed interface GameDialogState {
    data class Rotate(
        val availableStates: List<DiceState>,
        val onStateSelected: (DiceState) -> Unit
    ): GameDialogState

    data class Reshuffle(
        val cards: List<Card>,
        val minSelectable: Int,
        val maxSelectable: Int,
        val onCardsSelected: (List<Card>) -> Unit
    ): GameDialogState

    data class CardPreview(
        val card: Card
    ): GameDialogState
}
