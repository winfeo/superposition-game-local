package io.github.winfeo.superpositiongame.ui.screen.elements

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.winfeo.superpositiongame.manager.CardsDoubleTapManager
import io.github.winfeo.superpositiongame.manager.CardsDragAndDropManager
import io.github.winfeo.superpositiongame.manager.CardsLongPressManager
import io.github.winfeo.superpositiongame.model.card.Card
import io.github.winfeo.superpositiongame.model.card.CardType
import io.github.winfeo.superpositiongame.model.game.GameState
import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
import io.github.winfeo.superpositiongame.ui.actor.card.CardActorBuilder
import kotlin.math.PI
import kotlin.math.sin

//Веер-карт игрока
class CardsFan( //TODO единый контроллер входных нажатий?
    private val playerId: String,
    private val stage: Stage,
    private val dragManager: CardsDragAndDropManager,
    private val doubleTapManager: CardsDoubleTapManager,
    private val longPressManager: CardsLongPressManager,
    private val cardActorBuilder: CardActorBuilder
) {
    private val cardActors = mutableListOf<CardActor>()
    private val baseY = -60f ///TODO переделать настройку (динамически от размера экрана сделать)
    private val fanHeight = 30f
    private val maxRotation = 15f

    fun render(state: GameState) {
        val cards = state.players[playerId]?.hand?: return
        Gdx.app.log("CARD","Рука: size=${cards.size} cards=${cards.map { it.id }}")
        syncActors(cards)
    }

    private fun syncActors(cards: List<Card>) {
//        if (cardActors.size == cards.size) return
//        if (cardActors.hashCode() == cards.hashCode()) return
        if (
            cardActors.size == cards.size &&
            cardActors.map { it.card.id } == cards.map { it.id }
        ) return

        clearActors()
        cards.forEach { card ->
            val actor = cardActorBuilder.buildCardActor(card)

            longPressManager.makeCardLongPressable(actor)
            when(card.type) { //TODO подумать, как улучшить (DRAG и TAP энам?)
                CardType.SWAP,
                CardType.KRONECKER_MULTIPLICATION,
                CardType.IDENTITY,
                CardType.BARRIER,
                CardType.RESHUFFLE -> {
                    doubleTapManager.makeCardTouchable(actor)
                }
                else -> {
                    dragManager.makeCardDraggable(actor)
                }
            }

            stage.addActor(actor)
            cardActors.add(actor)
            Gdx.app.log("CARD", "позиция: = x=${actor.x}, y=${actor.y}, size=${actor.width}x${actor.height}")
        }

        renderFan(cardActors)
    }

    private fun clearActors() { //TODO переделать, не вызывать метод менеджера
//        cardActors.forEach { it.remove() }
        cardActors.forEach { card ->
            doubleTapManager.removeCardTouchable(card)
            longPressManager.removeCardLongPressable(card)
            card.remove()
        }
        cardActors.clear()
    }

    private fun renderFan(cards: List<CardActor>) {
        if (cards.isEmpty()) return

        val count = cards.size
        val stageWidth = stage.viewport.worldWidth
        val spacing = stageWidth / (count + 1)

        cards.forEachIndexed { index, card ->
            val x = spacing * (index + 1) - card.width / 2
            val t = index.toFloat() / (count - 1).coerceAtLeast(1)

            val yOffset = sin(t * PI).toFloat() * fanHeight
            val y = baseY + yOffset

            card.setOrigin(card.width / 2, 0f)
            card.setPosition(x, y)
            val rotation = (t - 0.5f) * 2 * maxRotation * -1
            card.rotation = rotation

            card.zIndex = 0
        }
    }

    fun consumeCard(card: CardActor) {
        doubleTapManager.removeCardTouchable(card)
        longPressManager.makeCardLongPressable(card)

        cardActors.remove(card)
        card.remove()

        renderFan(cardActors)
    }
}
