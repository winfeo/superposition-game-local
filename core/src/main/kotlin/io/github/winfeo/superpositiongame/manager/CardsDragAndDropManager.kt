package io.github.winfeo.superpositiongame.manager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import io.github.winfeo.superpositiongame.PlayerActionController
import io.github.winfeo.superpositiongame.ui.actor.SlotActor
import io.github.winfeo.superpositiongame.ui.actor.SlotActorStates
import io.github.winfeo.superpositiongame.ui.actor.card.CardActor

//Drag and drop механика
class CardsDragAndDropManager(
    private val controller: PlayerActionController
) {
    private val libgdxDragDrop = DragAndDrop()

    fun makeCardDraggable(card: CardActor) {
        val source = object: DragAndDrop.Source(card) {
            override fun dragStart(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int
            ): DragAndDrop.Payload {
                println("Отладка. Старт драга. Взяли за: x=$x y=$y")
                ///TODO сделать проверку, может ли игрок перетаскивать карту (GameState.phase + playerId)
                if (!card.canDrag) return DragAndDrop.Payload()

                val payload = DragAndDrop.Payload()
                payload.`object` = card

                val dragVisual = createDragVisual(card)
                payload.dragActor = dragVisual
                payload.dragActor.setSize(card.width / 1.5f, card.height / 1.5f)

                libgdxDragDrop.setDragActorPosition(card.width - x, -y)

                return payload
            }

//            override fun dragStop(
//                event: InputEvent,
//                x: Float,
//                y: Float,
//                pointer: Int,
//                payload: DragAndDrop.Payload?,
//                target: DragAndDrop.Target?
//            ) {}
        }

        libgdxDragDrop.addSource(source)
    }

    fun makeSlotTarget(slot: SlotActor) {
        val targetObj = object: DragAndDrop.Target(slot) {
            override fun drag(
                source: DragAndDrop.Source,
                payload: DragAndDrop.Payload,
                x: Float,
                y: Float,
                pointer: Int
            ): Boolean {
                val card = payload.`object` as? CardActor ?: return false

                val canDrop = controller.canDrop(card, slot)
                println("Проверка правил. ${canDrop.message}")
                Gdx.app.log("DragAndDrop", "Ошибка: ${canDrop.message}")
                slot.setNewState(canDrop.activeState)
                return canDrop.canDrop
            }

            override fun drop(
                source: DragAndDrop.Source,
                payload: DragAndDrop.Payload,
                x: Float,
                y: Float,
                pointer: Int
            ) {
                println("Отладка. Дроп карты")
                val card = payload.`object` as CardActor
                controller.dropCard(card, slot)
            }

            override fun reset(
                source: DragAndDrop.Source?,
                payload: DragAndDrop.Payload?
            ) {
                slot.setNewState(SlotActorStates.NO_ACTION)
            }
        }

        libgdxDragDrop.addTarget(targetObj)
    }

    private fun createDragVisual(card: CardActor): Image {
        return Image(card.drawable).apply {
            setSize(card.width, card.height)
            color.a = 0.7f
        }
    }

    fun clear() {
        libgdxDragDrop.clear()
    }
}
