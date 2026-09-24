package io.github.winfeo.superpositiongame.ui.screen.elements

import com.badlogic.gdx.scenes.scene2d.ui.Table
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.CardsDragAndDropManager
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.ui.actor.SlotActor
import io.github.winfeo.superpositiongame.ui.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.ui.actor.dice.DiceActorBuilder

// Создание структуры слотов для отображения игральных карт
// TODO Передаётся общее количество ячеек (пока 1 ряд из 4 карт)
class GameTable(
    private val playerId: String,
    private val dragManager: CardsDragAndDropManager,
    private val cardActorBuilder: CardActorBuilder,
    private val diceActorBuilder: DiceActorBuilder
//    private val playerActionController: PlayerActionController
): Table() {
    private val playerSlots = mutableListOf<SlotActor>()
    private val opponentSlots = mutableListOf<SlotActor>()
    private val cardsPadding = GameConfig.getCardsPadding()
    private val tablesPadding = GameConfig.getTablesPadding()

    init {
        setUpTable()
        createLayouts()

        //debugAll()
    }

    private fun setUpTable() {
        setFillParent(true)
        defaults().pad(tablesPadding.also { println("TablePadding: $it") }) //расс-ние между рядами
    }

    private fun createLayouts() {
        ///TODO сделать не через добавление add на сцену, а через добавление доп актора?
        add(createOpponentSlots())
            .fillX()
            .row()
        add(createPlayerSlots())
            .fillX()
    }

    private fun createOpponentSlots(): Table {
        val opponentRow = Table()
        opponentRow.defaults().space(cardsPadding)

        repeat(GameConfig.getSlotsOnTableAmount()) { index ->
            val slot = SlotActor(
                slotIndex = index,
                slotOwner = SlotOwner.OPPONENT,
                cardActorBuilder = cardActorBuilder,
                diceActorBuilder = diceActorBuilder
            )
            dragManager.makeSlotTarget(slot)
            opponentSlots.add(slot)
            opponentRow.add(slot)
        }

        return opponentRow
    }

    private fun createPlayerSlots(): Table {
        val playerRow = Table()
        playerRow.defaults().space(cardsPadding)

        repeat(GameConfig.getSlotsOnTableAmount()) { index ->
            val slot = SlotActor(
                slotIndex = index,
                slotOwner = SlotOwner.PLAYER,
                cardActorBuilder = cardActorBuilder,
                diceActorBuilder = diceActorBuilder
            )
            dragManager.makeSlotTarget(slot)
            playerSlots.add(slot)
            playerRow.add(slot)
        }

        return playerRow
    }

    fun render(state: GameState) {
        val player = state.players[playerId]?: return
        val opponent = state.players.values.first { it != player }

        player.slots.forEachIndexed { index, state ->
            playerSlots[index].render(state)
        }
        opponent.slots.forEachIndexed { index, state ->
            opponentSlots[index].render(state)
        }
    }

    fun getAllSlots(): List<SlotActor> {
        return playerSlots + opponentSlots
    }

}
