package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import io.github.winfeo.superpositiongame.graphics.Dialogs
import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
import java.util.Timer
import java.util.TimerTask

class CardsLongPressManager(
    private val dialogs: Dialogs
) {
    private val listeners = mutableMapOf<CardActor, ClickListener>()
    private val longPressDuration = 500L
    private var longPressTimer: Timer? = null
    fun makeCardLongPressable(cardActor: CardActor) {
        val listener = object : ClickListener() {

            override fun touchDown(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {

                longPressTimer?.cancel()
                longPressTimer = Timer().apply {
                    schedule(object: TimerTask() {
                        override fun run() {
                            dialogs.showCardPreview(cardActor.card)
                        }
                    }, longPressDuration)
                }
                return true
            }

            override fun touchUp(
                event: InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ) {
                longPressTimer?.cancel()
                longPressTimer = null
            }
        }

        cardActor.addListener(listener)
        listeners[cardActor] = listener
    }

    fun removeCardLongPressable(cardActor: CardActor) {
        listeners[cardActor]?.let { cardActor.removeListener(it) }
        listeners.remove(cardActor)
    }

    fun clear() {
        listeners.keys.toList().forEach { removeCardLongPressable(it) }
        longPressTimer?.cancel()
        longPressTimer = null
    }
}
