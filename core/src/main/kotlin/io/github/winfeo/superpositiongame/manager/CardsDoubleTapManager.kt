package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.PlayerActionController
import io.github.winfeo.superpositiongame.ui.actor.card.CardActor

class CardsDoubleTapManager(
    private val controller: PlayerActionController,
    private val onCardConsumed: (CardActor) -> Unit
) {
    private val clickListeners = mutableMapOf<CardActor, ClickListener>()
    private val lastTapTimes = mutableMapOf<CardActor, Long>()
    private val doubleTapInterval = GameConfig.getDoubleTapIntervalTime()

    fun makeCardTouchable(card: CardActor) {
        if (clickListeners.containsKey(card)) return

        val listener = object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                handleCardClick(card)
            }
        }

        card.addListener(listener)
        clickListeners[card] = listener
    }

    private fun handleCardClick(card: CardActor) {
        val currentTime = System.currentTimeMillis()
        val lastTime = lastTapTimes[card]

        if (lastTime == null || currentTime - lastTime > doubleTapInterval) {
            lastTapTimes[card] = currentTime
//            listener.onCardTapped(card)
        } else {
            lastTapTimes.remove(card)
            onDoubleTap(card)
//            removeCardTouchable(card)
//            listener.onCardDoubleTapped(card)
//            card.alpha = 0.3f
        }
    }

    private fun onDoubleTap(card: CardActor) {
        Gdx.app.log("TAP", "Карта используется: ${card.card.type}")
        onCardConsumed(card)
        controller.applyDoubleTapCard(card)
    }

    fun removeCardTouchable(card: CardActor) {
        clickListeners[card]?.let { listener ->
            card.removeListener(listener)
        }
        clickListeners.remove(card)
        lastTapTimes.remove(card)
    }

    fun clear() {
        clickListeners.forEach { (card, listener) ->
            card.removeListener(listener)
        }
        clickListeners.clear()
        lastTapTimes.clear()
    }

}
