package io.github.winfeo.superpositiongame.graphics

import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState

//Диалоги во время игры (имплементация в android-модуле)
interface Dialogs {
    //Для Rotate-карты
    fun showRotateCardDialog(
        availableStates: List<DiceState>,
        onStateSelected: (DiceState) -> Unit
    )

    fun showReshuffleDialog(
        cards: List<Card>,
        maxSelectable: Int = 4,
        minSelectable: Int = 1,
        onCardsSelected: (List<Card>) -> Unit
    )

    fun showCardPreview(
        card: Card
    )
}
