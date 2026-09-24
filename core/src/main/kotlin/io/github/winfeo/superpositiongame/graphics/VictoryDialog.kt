package io.github.winfeo.superpositiongame.graphics

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog

object VictoryDialog {
    fun showVictoryDialog(stage: Stage) {
        val skin = GameSkinFactory.createVictoryDialogSkin(stage)
        val dialog = Dialog("", skin).apply {
            text("You win!")

            button("OK") { hide() }

            setModal(true)
            setMovable(false)

            //pad(20f)
        }

        dialog.show(stage)
    }
}
