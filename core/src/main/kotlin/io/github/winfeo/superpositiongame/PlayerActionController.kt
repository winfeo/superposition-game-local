package io.github.winfeo.superpositiongame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.manager.SwapSelectionManager
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.card.description.AxisRotation
import io.github.winfeo.superpositiongame.model.dice.DiceState
import io.github.winfeo.superpositiongame.model.game.GameMoveType
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.model.game.Move
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.rule.RuleEngine
import io.github.winfeo.superpositiongame.rule.model.RuleContext
import io.github.winfeo.superpositiongame.rule.model.ValidationResult
import io.github.winfeo.superpositiongame.rule.rule.RotateCompatibilityRule
import io.github.winfeo.superpositiongame.ui.actor.SlotActor
import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
import kotlinx.coroutines.CoroutineScope

//Проверят можно ли разместить карту в слоте и отправляет Move на сервер
class PlayerActionController(
    private val playerId: String,
    private val dialogs: Dialogs,
    private val onMove: (Move) -> Unit,
    private val getGameState: () -> GameState,
    private val swapManager: SwapSelectionManager
) {

    fun canDrop(
        cardActor: CardActor,
        slotActor: SlotActor
    ): ValidationResult {
        val gameState = getGameState()
        val player = gameState.players[playerId]
        val opponent = gameState.players.values.first { it.id != playerId }

        ///TODO в утилиту?
        val targetSlot =
            if (slotActor.slotOwner == SlotOwner.PLAYER) player!!.slots[slotActor.slotIndex]
            else opponent.slots[slotActor.slotIndex]

        val targetPlayerId = //TODO временно для отладки
            if (slotActor.slotOwner == SlotOwner.PLAYER) player
            else opponent

        val context = RuleContext(
            card = cardActor.card,
            targetSlot = targetSlot,
            playerSlots = player!!.slots,
            opponentSlots = opponent.slots,
            activeSlotsRow = gameState.activeSlotsRow,
        )
        Gdx.app.log("MULTI_BEFORE", "Владелец слота: ${slotActor.slotOwner}\n" +
            "Активные ряды: ${gameState.activeSlotsRow}\n" +
            "Таргет: $targetSlot\n" +
            "Id игрока: $playerId\n" +
            "Id таргет игрока: ${targetPlayerId?.id}"
        )

        return RuleEngine.checkRules(context)
    }

    fun dropCard(
        cardActor: CardActor,
        slotActor: SlotActor
    ) {
        val state = getGameState()

        if (state.currentPlayerId != playerId) return
        val expectedTurnNumber = state.turnNumber

        when(cardActor.card.type) { //TODO сделать только drop карты
            CardType.ROTATE -> handleRotateCard(cardActor = cardActor, slotActor = slotActor, expectedTurnNumber = expectedTurnNumber)
            else -> {
                val move = createMove(cardActor = cardActor, slotActor = slotActor, expectedTurnNumber = expectedTurnNumber)
                onMove(move)
            }
        }
    }

    private fun handleRotateCard(
        cardActor: CardActor,
        slotActor: SlotActor,
        expectedTurnNumber: Int
    ) {
        val availableStates = getRotateDiceStates(
            cardAxis = cardActor.card.axis!!,
            diceState = slotActor.getDiceActor().dice.state
        )

        val checkRule = RotateCompatibilityRule()
        val checkResult = checkRule.check(
            dice = slotActor.getDiceActor().dice,
            card = cardActor.card
        )
        if (checkResult) {
            dialogs.showRotateCardDialog(
                availableStates = availableStates,
                onStateSelected = { selectedState ->
                    if (!isTurnStillActive(expectedTurnNumber)) return@showRotateCardDialog

                    val targetPlayerId = figureOutPlayerSide(slotActor)
                    val move = Move.RotateDice(
                        playerId = playerId,
                        expectedTurnNumber = expectedTurnNumber,
                        cardId = cardActor.card.id,
                        targetSlotIndex = slotActor.slotIndex,
                        newState = selectedState,
                        targetPlayerId = targetPlayerId
                    )

                    onMove(move)
                }
            )
        } else { //если не та ось вращения, просто оставляем состояние то же
            val targetPlayerId = figureOutPlayerSide(slotActor)
            val move = Move.RotateDice(
                playerId = playerId,
                expectedTurnNumber = expectedTurnNumber,
                cardId = cardActor.card.id,
                targetSlotIndex = slotActor.slotIndex,
                newState = slotActor.getDiceActor().dice.state,
                targetPlayerId = targetPlayerId
            )

            onMove(move)
        }
    }

    private fun createMove(
        cardActor: CardActor,
        slotActor: SlotActor,
        expectedTurnNumber: Int
    ): Move {
        val targetPlayerId = figureOutPlayerSide(slotActor)
        return Move.PlayCard(
            playerId = playerId,
            type = GameMoveType.PLAY_CARD,
            expectedTurnNumber = expectedTurnNumber,
            cardId = cardActor.card.id,
            targetSlotIndex = slotActor.slotIndex,
            targetPlayerId = targetPlayerId
        )
    }

    private fun figureOutPlayerSide(slotActor: SlotActor): String {
        return if (slotActor.slotOwner == SlotOwner.PLAYER) SlotOwner.PLAYER.name else SlotOwner.OPPONENT.name
    }

    ///TODO в утилиту вынести?
    private fun getRotateDiceStates(
        cardAxis: AxisRotation,
        diceState: DiceState
    ): List<DiceState> {
        return when (cardAxis) {
            AxisRotation.X -> listOf(DiceState.ZERO, DiceState.I, DiceState.ONE, DiceState.I_MINUS)
            AxisRotation.Y -> listOf(DiceState.ZERO, DiceState.PLUS, DiceState.ONE, DiceState.MINUS)
            AxisRotation.Z -> listOf(DiceState.PLUS, DiceState.I, DiceState.MINUS, DiceState.I_MINUS)
        }.filter { it != diceState }
    }

    fun applyDoubleTapCard(cardActor: CardActor) {
        val state = getGameState()

        if (state.currentPlayerId != playerId) return
        val expectedTurnNumber = state.turnNumber

        when(cardActor.card.type) { //TODO сделать только tap карты
            CardType.SWAP -> {
                swapManager.startSelection { first, second ->
                    if (!isTurnStillActive(expectedTurnNumber)) {
                        swapManager.reset()
                        return@startSelection
                    }

                    val move = Move.SwapDices(
                        playerId = playerId,
                        expectedTurnNumber = expectedTurnNumber,
                        cardId = cardActor.card.id,
                        firstSlotIndex = first.slotIndex,
                        secondSlotIndex = second.slotIndex,
                        firstSlotOwner = first.slotOwner.name,
                        secondSlotOwner = second.slotOwner.name
                    )

                    onMove(move)
                }
            }

            CardType.RESHUFFLE -> {
                val gameState = getGameState()
                val player = gameState.players[playerId] ?: return

                val handCards = player.hand.filter { it.id != cardActor.card.id }

                if (handCards.isEmpty()) {
                    Gdx.app.log("RESHUFFLE", "Отладка. Все карты использованы")
                    return
                }

                dialogs.showReshuffleDialog(
                    cards = handCards,
                    maxSelectable = minOf(4, handCards.size),
                    minSelectable = 1,
                    onCardsSelected = { selectedCards ->
                        if (!isTurnStillActive(expectedTurnNumber)) return@showReshuffleDialog

                        val move = Move.ReshuffleCard(
                            playerId = playerId,
                            expectedTurnNumber = expectedTurnNumber,
                            cardId = cardActor.card.id,
                            cardsToChange = selectedCards.map { it.id }
                        )
                        onMove(move)
                    }
                )
            }
            else -> {
                val move = createDoubleTapMove(card = cardActor.card, expectedTurnNumber = expectedTurnNumber)
                onMove(move)
            }
        }

    }

    private fun createDoubleTapMove(
        card: Card,
        expectedTurnNumber: Int
    ): Move {
        return Move.DoubleTapEffect(
            playerId = playerId,
            expectedTurnNumber = expectedTurnNumber,
            cardId = card.id
        )
    }

    private fun isTurnStillActive(expectedTurnNumber: Int): Boolean {
        val state = getGameState()

        return state.currentPlayerId == playerId && state.turnNumber == expectedTurnNumber && state.turnEndsAt > 0L
    }

}
