package io.github.winfeo.superpositiongame.ui.actor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import io.github.winfeo.superpositiongame.ui.actor.card.CardActorBuilder
import io.github.winfeo.superpositiongame.ui.actor.dice.DiceActorBuilder
import io.github.winfeo.superpositiongame.model.game.SlotState
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.graphics.BorderTexture
import io.github.winfeo.superpositiongame.model.game.SlotOwner
import io.github.winfeo.superpositiongame.ui.actor.card.CardActor
import io.github.winfeo.superpositiongame.ui.actor.dice.DiceActor
import kotlin.math.sin

//Ячейка таблицы (представление дайса и слота карты)
class SlotActor(
    private val diceActorBuilder: DiceActorBuilder,
    private val cardActorBuilder: CardActorBuilder,
    val slotIndex: Int,
    ///TODO добавить SlotState?
    val slotOwner: SlotOwner
): Group() {
    private val borderImage = Image(BorderTexture.getBorderTexture())
    private var state = SlotActorStates.NO_ACTION
    private var pulseTime = 0f ///TODO добавить в конфиг?
    private val pulseSpeed = 8f
    private var diceActor: DiceActor? = null
    private var cardActor: CardActor? = null

    init {
        val with = GameConfig.cardWidth
        val height = GameConfig.cardHeight
//        defaults().pad(5f) //отступ от границы

        setSize(with, height)

        borderImage.setSize(
            GameConfig.cardWidth,
            GameConfig.cardHeight
        )

        borderImage.setPosition(0f, 0f)
        borderImage.color = Color.valueOf("aeb0a7") ///TODO переделать
        addActor(borderImage)
        borderImage.setZIndex(1)
    }

    fun render(state: SlotState) {
        renderDice(state)
        renderCard(state)
    }

    private fun renderDice(state: SlotState) {
        if (diceActor == null) {
            diceActor = diceActorBuilder.buildDiceActor(state.dice)

            diceActor!!.setSize(
                GameConfig.getDiceSide(),
                GameConfig.getDiceSide()
            )

            diceActor!!.setPosition(
                -GameConfig.getDiceSide() / 2f,
                GameConfig.cardHeight - GameConfig.getDiceSide() / 2f
            )

            addActor(diceActor)
            diceActor!!.setZIndex(2)
        } else {
            diceActor!!.render(state.dice)
        }
    }

    private fun renderCard(state: SlotState) {
        val lastCard = state.appliedCards.lastOrNull()
        if (lastCard == null) {
            cardActor?.remove()
            cardActor = null
            return
        }

        if (cardActor == null) {
            cardActor = cardActorBuilder.buildCardActor(lastCard)
            val cardInset = GameConfig.getCardBorderThickness()

            cardActor!!.setSize(
                GameConfig.cardWidth - cardInset * 2f,
                GameConfig.cardHeight - cardInset * 2f
            )

            cardActor!!.setPosition(cardInset, cardInset)
            addActor(cardActor)
            cardActor!!.setZIndex(0)
        } else {
            cardActor!!.render(lastCard)
        }
    }

    fun setNewState(newState: SlotActorStates) {
        if (state == newState) return

        state = newState
        borderImage.color = when (state) {
//            SlotActorStates.NO_ACTION -> Color.GOLD
            SlotActorStates.NO_ACTION -> Color.valueOf("aeb0a7")
            SlotActorStates.HOVERED_CAN_PLACE -> Color.CYAN
            SlotActorStates.HOVERED_CANT_PLACE -> Color.RED
            SlotActorStates.REQUIRED_DICE_STATE -> Color.GREEN
        }

        pulseTime = 0f
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (state != SlotActorStates.NO_ACTION) {
            borderImage.color.a = 0.7f + 0.3f * sin(pulseTime)
            pulseTime += delta * pulseSpeed
        }
    }

    fun getDiceActor(): DiceActor = diceActor!!

//    override fun draw(batch: Batch, parentAlpha: Float) {
//        super.draw(batch, parentAlpha)
//
//        val oldColor = batch.color
//        val newColor = Color(borderColor)
//        if (state != SlotActorStates.NO_ACTION &&
//            state != SlotActorStates.REQUIRED_DICE_STATE) {
//            val pulseAlpha = 0.7f + 0.3f * sin(pulseTime)
//            newColor.a = pulseAlpha * parentAlpha
//        }
//        else {
//            newColor.a = borderColor.a * parentAlpha
//        }
//
//        batch.color = newColor
//        batch.draw(borderTexture, x, y, width, height)
//        batch.color = oldColor
//    }
}
