package io.github.winfeo.superpositiongame.android.ui.screen.game

import androidx.lifecycle.ViewModel
import io.github.winfeo.superpositiongame.android.domain.game.GameRepository
import io.github.winfeo.superpositiongame.android.domain.game.usecase.ObserveBoardGameUseCase
import io.github.winfeo.superpositiongame.android.domain.game.usecase.SelectBoardTargetUseCase
import io.github.winfeo.superpositiongame.android.domain.game.usecase.GiveUpBoardGameUseCase
import io.github.winfeo.superpositiongame.android.domain.game.usecase.ReturnToBoardLobbyUseCase
import io.github.winfeo.superpositiongame.android.ui.dialog.game.GameDialogState
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.dice.DiceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    private val repository: GameRepository
) : ViewModel() {
    private val observeBoardGame = ObserveBoardGameUseCase(repository)
    private val selectBoardTarget = SelectBoardTargetUseCase(repository)
    private val giveUpBoardGame = GiveUpBoardGameUseCase(repository)
    private val returnToBoardLobby = ReturnToBoardLobbyUseCase(repository)

    private val _dialogState = MutableStateFlow<GameDialogState?>(null)
    val dialogState = _dialogState.asStateFlow()

    val session = observeBoardGame()

    fun selectTarget(playerIndex: Int, cubitIndex: Int): Boolean {
        return selectBoardTarget(playerIndex, cubitIndex)
    }

    fun giveUp(): Boolean {
        return giveUpBoardGame()
    }

    fun returnToLobby() {
        returnToBoardLobby()
    }

    fun showRotateCardDialog(
        availableStates: List<DiceState>,
        onStateSelected: (DiceState) -> Unit
    ) {
        _dialogState.value = GameDialogState.Rotate(availableStates, onStateSelected)
    }

    fun showReshuffleDialog(
        cards: List<Card>,
        minSelectable: Int,
        maxSelectable: Int,
        onCardsSelected: (List<Card>) -> Unit
    ) {
        _dialogState.value = GameDialogState.Reshuffle(
            cards = cards,
            maxSelectable = maxSelectable,
            minSelectable = minSelectable,
            onCardsSelected = onCardsSelected
        )
    }

    fun showCardPreview(card: Card) {
        _dialogState.value = GameDialogState.CardPreview(card)
    }

    fun dismissDialog() {
        _dialogState.value = null
    }
}
