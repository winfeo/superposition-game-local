package io.github.winfeo.superpositiongame.graphics

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import io.github.winfeo.superpositiongame.config.GameConfig
import io.github.winfeo.superpositiongame.manager.DiceAtlasManager
import io.github.winfeo.superpositiongame.model.dice.DiceState
import kotlinx.coroutines.suspendCancellableCoroutine

//Выбор состояния кубита для Rotate гейтов (диалоговое окно)
///TODO не переисовывается при изменении размера экрана
class RotateSelectionDialog {
    suspend fun show(
        stage: Stage,
        availableStates: List<DiceState>
    ): DiceState = suspendCancellableCoroutine { cont ->
        val skin = GameSkinFactory.createSelectionDialogSkin(stage)

        val dialog = Dialog("", skin).apply {
            setModal(true)
            setMovable(false)
        }

        val gridTable = Table()
        gridTable.defaults().pad(GameConfig.getCardsPadding())

        availableStates.forEach { state ->
            val stateTexture = DiceAtlasManager.getStateTexture(state.textureId)
            val image = Image(stateTexture)

            image.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    if (cont.isActive) {
                        cont.resume(state) {}
                    }
                    dialog.hide()
                }
            })

            val diceSide = GameConfig.cardWidth
            gridTable.add(image).size(diceSide, diceSide)
        }

        dialog.contentTable.add(gridTable)
        dialog.show(stage)
    }
}
