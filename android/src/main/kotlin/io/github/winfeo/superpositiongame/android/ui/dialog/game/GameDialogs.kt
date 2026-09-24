package io.github.winfeo.superpositiongame.android.ui.dialog.game

import io.github.winfeo.superpositiongame.android.ui.screen.game.GameViewModel
import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState

class GameDialogs(
    private val viewModel: GameViewModel
): Dialogs {

    override fun showRotateCardDialog(
        availableStates: List<DiceState>,
        onStateSelected: (DiceState) -> Unit
    ) {
        viewModel.showRotateCardDialog(
            availableStates = availableStates,
            onStateSelected = onStateSelected
        )


    }

    override fun showReshuffleDialog(
        cards: List<Card>,
        maxSelectable: Int,
        minSelectable: Int,
        onCardsSelected: (List<Card>) -> Unit
    ) {
        viewModel.showReshuffleDialog(
            cards = cards,
            maxSelectable = maxSelectable,
            minSelectable = minSelectable,
            onCardsSelected = onCardsSelected
        )
    }

    override fun showCardPreview(card: Card) {
        viewModel.showCardPreview(card)
    }
}
